package com.CodeExamner.repository;

import com.CodeExamner.entity.Submission;
import com.CodeExamner.entity.enums.JudgeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // --- 分页查询方法 ---
    Page<Submission> findByStudentId(Long studentId, Pageable pageable);
    Page<Submission> findByProblemId(Long problemId, Pageable pageable);
    Page<Submission> findByProblemIdAndStudentId(Long problemId, Long studentId, Pageable pageable);

    // --- 考试相关查询 ---
    List<Submission> findByExamIdAndStudentId(Long examId, Long studentId);

    @Query("SELECT s FROM Submission s WHERE s.exam.id = :examId")
    Page<Submission> findByExamId(Long examId, Pageable pageable);

    @Query("SELECT s FROM Submission s WHERE s.exam.id = :examId AND s.student.id = :studentId")
    Page<Submission> findByExamIdAndStudentId(Long examId, Long studentId, Pageable pageable);

    // --- 统计方法 ---

    // 按状态统计
    Long countByStatus(JudgeStatus status);

    // 按学生统计 (已存在，但这里明确用于仪表盘统计)
    Long countByStudentId(Long studentId);
    Long countByStudentIdAndStatus(Long studentId, JudgeStatus status);
    Long countByStudentIdAndSubmitTimeAfter(Long studentId, LocalDateTime submitTime);

    // 按题目统计
    Long countByProblemId(Long problemId);
    Long countByProblemIdAndStatus(Long problemId, JudgeStatus status);

    // 其他统计
    Long countByExamId(Long examId);
    Long countByExamIdAndStatus(Long examId, JudgeStatus status);
    
    // ==========================================================
    // --- 🎯 学生仪表盘所需的新查询 ---
    // ==========================================================

    /**
     * 【学生仪表盘 - 最近提交列表】
     * 获取指定学生最近的提交记录列表。
     * 使用 Spring Data JPA 的命名查询，按提交时间降序排列，并利用 Pageable 限制记录数。
     * @param studentId 学生ID
     * @param pageable 用于限制返回的记录数（例如 TOP 5）
     * @return 提交记录列表，按提交时间降序排列
     */
    List<Submission> findByStudentIdOrderBySubmissionTimeDesc(Long studentId, Pageable pageable);
    
    /**
     * 【学生仪表盘 - 最高得分率】
     * 查找指定学生所有提交中获得的最高得分率（scoreRate 字段，范围 0.0 - 100.0）。
     * @param studentId 学生ID
     * @return 最高得分率，如果没有记录则返回 Optional<Double>
     */
    @Query("SELECT MAX(s.scoreRate) FROM Submission s WHERE s.student.id = ?1")
    Optional<Double> findMaxScoreRateByStudentId(Long studentId);
}