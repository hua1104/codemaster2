package com.CodeExamner.controller;

import com.CodeExamner.dto.DashboardStatsDTO;
import com.CodeExamner.dto.ExamDTO;
import com.CodeExamner.dto.SubmissionDTO;
import com.CodeExamner.service.StudentDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/dashboard")
public class StudentDashboardController {

    private final StudentDashboardService dashboardService;

    public StudentDashboardController(StudentDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Helper method to extract student ID from UserDetails.
     * In a real application, you might cast UserDetails to a custom type (like UserPrincipal)
     * which holds the Long ID. Here, we assume the username is the ID or can be used to find it.
     */
    private Long getCurrentStudentId(@AuthenticationPrincipal UserDetails userDetails) {
        // 假设您的 UserDetails 的 getUsername() 方法返回的是学生的 Long ID 的字符串表示。
        // 或者您需要通过 username (通常是学号/邮箱) 查询 User 实体来获取 Long ID。
        // 暂时使用一个默认 ID，直到认证系统完善：
        // return Long.valueOf(userDetails.getUsername()); // 真实实现
        return 1L; // 临时 Mock ID
    }


    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStudentStats(@AuthenticationPrincipal UserDetails userDetails) {
        Long studentId = getCurrentStudentId(userDetails);
        DashboardStatsDTO stats = dashboardService.getStudentStats(studentId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/upcoming-exams")
    public ResponseEntity<List<ExamDTO>> getUpcomingExams(@AuthenticationPrincipal UserDetails userDetails) {
        Long studentId = getCurrentStudentId(userDetails);
        List<ExamDTO> exams = dashboardService.getUpcomingExams(studentId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/recent-submissions")
    public ResponseEntity<List<SubmissionDTO>> getRecentSubmissions(@AuthenticationPrincipal UserDetails userDetails) {
        Long studentId = getCurrentStudentId(userDetails);
        List<SubmissionDTO> submissions = dashboardService.getRecentSubmissions(studentId);
        return ResponseEntity.ok(submissions);
    }
}