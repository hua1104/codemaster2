package com.CodeExamner.repository;

import com.CodeExamner.entity.Exam;
import com.CodeExamner.entity.enums.ExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    // --- 教师/管理员常用的查询 ---

    /**
     * 按状态查询（不分页）
     */
    List<Exam> findByStatus(ExamStatus status);

    /**
     * 按状态列表查询（不分页）
     */
    List<Exam> findByStatusIn(List<ExamStatus> statuses);

    /**
     * 按创建者查询（不分页）
     */
    List<Exam> findByCreatedById(Long teacherId);

    /**
     * 查询进行中的考试 (仅查询状态为 ONGOING 且时间在开始和结束时间之间)
     */
    @Query("SELECT e FROM Exam e WHERE e.status = 'ONGOING' AND :currentTime BETWEEN e.startTime AND e.endTime")
    List<Exam> findOngoingExams(LocalDateTime currentTime);

    /**
     * 按创建者查询（分页版本）
     */
    Page<Exam> findByCreatedById(Long teacherId, Pageable pageable);

    /**
     * 按状态列表查询（分页版本）
     */
    Page<Exam> findByStatusIn(List<ExamStatus> statuses, Pageable pageable);

    /**
     * 统计指定状态的考试数量
     */
    Long countByStatus(ExamStatus status);


    // --- 学生仪表盘所需的查询 ---

    /**
     * 【学生仪表盘 - 待考场次】
     * 统计指定学生ID，且开始时间在当前时间之后（即尚未开始）的考试数量。
     * @param studentId 学生ID
     * @param currentTime 当前时间
     */
    @Query(value = "SELECT COUNT(e.id) FROM exam e JOIN student_exam se ON e.id = se.exam_id WHERE se.student_id = ?1 AND e.start_time > ?2",
           nativeQuery = true)
    long countPendingExamsByStudentId(Long studentId, LocalDateTime currentTime);

    /**
     * 【学生仪表盘 - 已完成考试】
     * 统计指定学生ID，且结束时间在当前时间之前（即已结束）的考试数量。
     * @param studentId 学生ID
     * @param currentTime 当前时间
     */
    @Query(value = "SELECT COUNT(e.id) FROM exam e JOIN student_exam se ON e.id = se.exam_id WHERE se.student_id = ?1 AND e.end_time < ?2",
           nativeQuery = true)
    long countFinishedExamsByStudentId(Long studentId, LocalDateTime currentTime);

    /**
     * 【学生仪表盘 - 即将开始列表】
     * 查找指定学生ID，且开始时间在当前时间之后（即尚未开始）的考试列表（用于Upcoming Exams List）。
     * @param studentId 学生ID
     * @param currentTime 当前时间
     * @param pageable 用于限制返回记录数（例如 TOP 5）
     */
    @Query(value = "SELECT e.* FROM exam e JOIN student_exam se ON e.id = se.exam_id WHERE se.student_id = ?1 AND e.start_time > ?2 ORDER BY e.start_time ASC",
           countQuery = "SELECT COUNT(e.id) FROM exam e JOIN student_exam se ON e.id = se.exam_id WHERE se.student_id = ?1 AND e.start_time > ?2",
           nativeQuery = true)
    Page<Exam> findUpcomingExamsByStudentId(Long studentId, LocalDateTime currentTime, Pageable pageable);
}