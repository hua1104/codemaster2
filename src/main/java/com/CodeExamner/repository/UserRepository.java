package com.CodeExamner.repository;

import com.CodeExamner.entity.User;
import com.CodeExamner.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 【认证/登录】通过用户名查找用户 (通常用于加载用户详情，但这里我们使用 ID)
     * 尽管我们用ID作为Spring Security的username，但保留此方法可能对其他业务有益
     */
    Optional<User> findByUsername(String username);

    /**
     * 【注册/校验】检查用户名是否已存在
     */
    boolean existsByUsername(String username);

    /**
     * 【注册/校验】检查邮箱是否已存在
     */
    boolean existsByEmail(String email);

    /**
     * 【系统统计】统计指定角色的用户数量
     */
    Long countByRole(UserRole role);

    /**
     * 【登录/查找】通过邮箱查找用户
     */
    Optional<User> findByEmail(String email);
}