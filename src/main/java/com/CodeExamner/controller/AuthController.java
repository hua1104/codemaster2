package com.CodeExamner.controller;

import com.CodeExamner.dto.request.LoginRequest;
import com.CodeExamner.dto.request.RegisterRequest;
import com.CodeExamner.dto.response.AuthResponse;
import com.CodeExamner.entity.Student;
import com.CodeExamner.entity.User;
import com.CodeExamner.entity.enums.UserRole;
import com.CodeExamner.repository.UserRepository;
import com.CodeExamner.security.JwtUtil;
import com.CodeExamner.security.UserPrincipal; // 导入 UserPrincipal
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 1. 尝试认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // 2. 将认证对象设置到 Security 上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 3. 🎯 修正点 1：获取 UserPrincipal，而不是 User 实体
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        // 4. 🎯 修正点 2：调用 JwtUtil.generateJwtToken() 方法，参数为 Authentication 对象
        String jwt = jwtUtil.generateJwtToken(authentication); 

        // 5. 返回 AuthResponse
        return ResponseEntity.ok(new AuthResponse(
                jwt, 
                userPrincipal.getId(), 
                userPrincipal.getEmail(), // 使用 email 代替 username
                userPrincipal.getAuthorities().iterator().next().getAuthority() // 获取权限字符串
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new AuthResponse("用户名已存在"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new AuthResponse("邮箱已被注册"));
        }

        User user;
        // 注册逻辑保持不变 (假设 Student 继承 User)
        if (registerRequest.getRole() == UserRole.STUDENT) {
            Student student = new Student();
            student.setStudentId(registerRequest.getStudentId());
            student.setRealName(registerRequest.getRealName());
            student.setClassName(registerRequest.getClassName());
            user = student;
        } else {
            user = new User();
        }

        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(registerRequest.getRole());

        User savedUser = userRepository.save(user);
        
        // 🎯 修正点 3：注册成功后，先进行认证，再生成 Token
        // 这里的逻辑有点绕，简单起见，我们直接创建一个临时的 Authentication 对象来生成 Token。
        // 因为注册是直接保存，所以此时没有密码可供 AuthenticationManager 认证。
        
        // 创建临时的 UserPrincipal 用于生成 Token
        UserPrincipal principal = UserPrincipal.build(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        String jwt = jwtUtil.generateJwtToken(authentication);

        return ResponseEntity.ok(new AuthResponse(
                jwt, 
                savedUser.getId(), 
                savedUser.getEmail(), 
                savedUser.getRole().name()
        ));
    }
}