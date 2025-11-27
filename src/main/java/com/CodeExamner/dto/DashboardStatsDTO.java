package com.CodeExamner.dto;

public class DashboardStatsDTO {
    private int pendingExams;
    private int finishedExams;
    private int totalSubmissions;
    private double maxScoreRate;

    public DashboardStatsDTO(int pendingExams, int finishedExams, int totalSubmissions, double maxScoreRate) {
        this.pendingExams = pendingExams;
        this.finishedExams = finishedExams;
        this.totalSubmissions = totalSubmissions;
        this.maxScoreRate = maxScoreRate;
    }
    
    public DashboardStatsDTO() {}
    
    // Getters and Setters
    public int getPendingExams() {
        return pendingExams;
    }
    public void setPendingExams(int pendingExams) {
        this.pendingExams = pendingExams;
    }
    public int getFinishedExams() {
        return finishedExams;
    }
    public void setFinishedExams(int finishedExams) {
        this.finishedExams = finishedExams;
    }
    public int getTotalSubmissions() {
        return totalSubmissions;
    }
    public void setTotalSubmissions(int totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }
    public double getMaxScoreRate() {
        return maxScoreRate;
    }
    public void setMaxScoreRate(double maxScoreRate) {
        this.maxScoreRate = maxScoreRate;
    }
}