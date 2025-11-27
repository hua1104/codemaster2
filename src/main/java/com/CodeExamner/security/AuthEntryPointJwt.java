package com.CodeExamner.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 处理未经授权的请求：当用户访问需要认证的资源但没有提供有效认证凭证时，
 * Spring Security 会调用此类的 commence 方法。
 * 作用是返回 HTTP 401 Unauthorized 响应。
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, 
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 记录未授权错误信息
        logger.error("Unauthorized error: {}", authException.getMessage());
        
        // 设置响应状态码为 401 Unauthorized，并发送错误信息
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}