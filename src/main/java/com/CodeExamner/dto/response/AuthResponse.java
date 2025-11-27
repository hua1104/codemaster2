package com.CodeExamner.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long id;
    private String email; // 使用邮箱作为用户标识
    private String role;
    private String type = "Bearer";
    private String message;

    // 成功登录/注册的构造函数
    public AuthResponse(String token, Long id, String email, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.role = role;
    }
    
    // 失败/错误的构造函数
    public AuthResponse(String message) {
        this.message = message;
    }
}