// controller/UserController.java
package com.CodeExamner.controller;

import com.CodeExamner.dto.response.UserProfileResponse;
import com.CodeExamner.entity.Student;
import com.CodeExamner.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * 获取当前登录用户的个人信息，前端对应 endpoints.auth.profile = /users/me
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication != null ? authentication.getPrincipal() : null;

        if (!(principal instanceof User)) {
            return ResponseEntity.status(401).build();
        }

        User user = (User) principal;
        UserProfileResponse profile = new UserProfileResponse();
        profile.setId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());
        profile.setRole(user.getRole());

        if (user instanceof Student) {
            Student student = (Student) user;
            profile.setStudentId(student.getStudentId());
            profile.setRealName(student.getRealName());
            profile.setClassName(student.getClassName());
        }

        return ResponseEntity.ok(profile);
    }
}


