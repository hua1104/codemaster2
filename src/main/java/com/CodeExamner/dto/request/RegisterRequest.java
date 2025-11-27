package com.CodeExamner.dto.request;

import com.CodeExamner.entity.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    @NotBlank
    @Email
    private String email;
    
    private UserRole role = UserRole.STUDENT; // 默认为学生
    
    // 仅学生需要
    private String studentId;
    private String realName;
    private String className;
}