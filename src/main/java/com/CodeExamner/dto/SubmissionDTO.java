package com.CodeExamner.dto;

public class SubmissionDTO {
    private Long id;
    private String problemTitle;
    private String examName;
    private int score;

    public SubmissionDTO(Long id, String problemTitle, String examName, int score) {
        this.id = id;
        this.problemTitle = problemTitle;
        this.examName = examName;
        this.score = score;
    }
    
    public SubmissionDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getProblemTitle() {
        return problemTitle;
    }
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }
    public String getExamName() {
        return examName;
    }
    public void setExamName(String examName) {
        this.examName = examName;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}