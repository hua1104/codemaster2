package com.CodeExamner.service;

import com.CodeExamner.dto.DashboardStatsDTO;
import com.CodeExamner.dto.ExamDTO;
import com.CodeExamner.dto.SubmissionDTO;

import java.util.List;

public interface StudentDashboardService {

    DashboardStatsDTO getStudentStats(Long studentId);

    List<ExamDTO> getUpcomingExams(Long studentId);

    List<SubmissionDTO> getRecentSubmissions(Long studentId);
}