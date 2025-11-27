package com.CodeExamner.service.impl;

import com.CodeExamner.dto.DashboardStatsDTO;
import com.CodeExamner.dto.ExamDTO;
import com.CodeExamner.dto.SubmissionDTO;
import com.CodeExamner.entity.Exam;
import com.CodeExamner.entity.Submission;
import com.CodeExamner.repository.ExamRepository;
import com.CodeExamner.repository.SubmissionRepository;
import com.CodeExamner.service.StudentDashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentDashboardServiceImpl implements StudentDashboardService {

    private final ExamRepository examRepository;
    private final SubmissionRepository submissionRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int LIST_LIMIT = 5; // 列表限制条数

    public StudentDashboardServiceImpl(
        ExamRepository examRepository, 
        SubmissionRepository submissionRepository
    ) {
        this.examRepository = examRepository;
        this.submissionRepository = submissionRepository;
    }

    // --- 实体到 DTO 的转换方法 ---

    /**
     * 将 Exam 实体转换为 ExamDTO，使用 getTitle() 代替原先的 getName()。
     */
    private ExamDTO toExamDTO(Exam exam) {
        if (exam == null) return null;
        return new ExamDTO(
            exam.getId(),
            exam.getTitle(), // ⭐ 修正点：使用 getTitle()
            exam.getStartTime().format(DATE_FORMATTER) 
        );
    }
    
    /**
     * 将 Submission 实体转换为 SubmissionDTO，并修正了 Exam 的字段名。
     */
    private SubmissionDTO toSubmissionDTO(Submission submission) {
        if (submission == null) return null;
        
        // 假设 Submission 实体具有 getProblem()、getExam() 和 getScore() 方法
        return new SubmissionDTO(
            submission.getId(),
            submission.getProblem().getTitle(), // 假设 Problem 实体有 getTitle()
            submission.getExam().getTitle(),     // ⭐ 修正点：使用 getExam().getTitle()
            submission.getScore()
        );
    }


    @Override
    public DashboardStatsDTO getStudentStats(Long studentId) {
        LocalDateTime now = LocalDateTime.now();
        
        // 1. 待考场次
        long pendingCount = examRepository.countPendingExamsByStudentId(studentId, now);
        
        // 2. 已完成考试
        long finishedCount = examRepository.countFinishedExamsByStudentId(studentId, now); 
        
        // 3. 已提交题目
        long submissionCount = submissionRepository.countByStudentId(studentId);
        
        // 4. 最高得分率
        double maxRate = submissionRepository.findMaxScoreRateByStudentId(studentId)
                                            .orElse(0.0); 

        return new DashboardStatsDTO(
            (int) pendingCount, 
            (int) finishedCount, 
            (int) submissionCount, 
            maxRate
        );
    }

    @Override
    public List<ExamDTO> getUpcomingExams(Long studentId) {
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, LIST_LIMIT);
        
        // 查询即将开始的考试列表
        List<Exam> exams = examRepository.findUpcomingExamsByStudentId(studentId, now, pageable).getContent();

        if (exams.isEmpty()) {
            return Collections.emptyList();
        }
        
        return exams.stream()
                    .map(this::toExamDTO)
                    .collect(Collectors.toList());
    }

    @Override
    public List<SubmissionDTO> getRecentSubmissions(Long studentId) {
        Pageable pageable = PageRequest.of(0, LIST_LIMIT);
        
        // 查询最近的提交记录
        List<Submission> submissions = submissionRepository.findByStudentIdOrderBySubmissionTimeDesc(studentId, pageable);
        
        if (submissions.isEmpty()) {
            return Collections.emptyList();
        }
        
        return submissions.stream()
                    .map(this::toSubmissionDTO)
                    .collect(Collectors.toList());
    }
}