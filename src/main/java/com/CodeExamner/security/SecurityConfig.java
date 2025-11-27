package com.CodeExamner.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // 引入 HttpMethod
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration; // 引入 CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource; // 引入 CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // 引入 UrlBasedCorsConfigurationSource

import java.util.Arrays; // 引入 Arrays
import java.util.List; // 引入 List

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    // 🎯 修正点 1：添加对 JWT 认证入口点的注入
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler; 

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * 🎯 修正点 2：添加 CORS 配置 Bean，允许跨域请求
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // 生产环境请限制
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 🎯 修正点 3：添加异常处理配置，使用 AuthEntryPointJwt
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // 使用上面定义的 CORS 配置
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 配置 Session 管理为无状态
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 授权规则配置
                .authorizeHttpRequests(authz -> authz
                        // 登录、注册
                        .requestMatchers("/auth/**").permitAll()
                        // CORS 预检请求
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                        // 公开问题列表
                        .requestMatchers("/problems/public").permitAll()
                        // 查看单个问题需要认证
                        .requestMatchers("/problems/{id}").authenticated() 
                        // 管理问题的接口需要 TEACHER 或 ADMIN 角色
                        .requestMatchers("/problems/**").hasAnyAuthority("TEACHER", "ADMIN")
                        // 考试相关
                        .requestMatchers("/exams/available", "/exams/ongoing").authenticated()
                        // 管理考试的接口需要 TEACHER 或 ADMIN 角色
                        .requestMatchers("/exams/**").hasAnyAuthority("TEACHER", "ADMIN") 
                        // 提交需要认证
                        .requestMatchers("/submissions/**").authenticated()
                        // 任何其他请求都需要认证
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}