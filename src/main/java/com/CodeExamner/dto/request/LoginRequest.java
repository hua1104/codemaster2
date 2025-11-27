package com.CodeExamner.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String username; // 登录标识，可以是用户名或邮箱
    
    @NotBlank
    private String password;
}