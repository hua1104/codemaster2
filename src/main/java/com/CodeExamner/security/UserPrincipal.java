package com.CodeExamner.security;

import com.CodeExamner.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
public class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username; // 用于存储用户的唯一标识，例如学号或邮箱（在我们的设计中是用户的 Long ID 的字符串形式）
    
    @JsonIgnore
    private String password;
    
    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String username, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * 将您的 User 实体转换为 Spring Security 识别的 UserPrincipal 对象
     */
    public static UserPrincipal build(User user) {
        // 假设您的 User 实体中有一个 getRole() 方法，返回 UserRole 枚举
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        // 我们将用户的 ID 作为 Spring Security 的 username 字段
        return new UserPrincipal(
                user.getId(),
                String.valueOf(user.getId()), // 🎯 将 ID 转换为 String 存储为 username
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    // --- UserDetails 接口方法实现 ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username; // 这里返回的是用户的 Long ID 的字符串形式
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 用于比较用户主体对象
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal user = (UserPrincipal) o;
        return Objects.equals(id, user.id);
    }
}
