package com.CodeExamner.dto;

public class ExamDTO {
    private Long id;
    private String name;
    private String startTime; 

    public ExamDTO(Long id, String name, String startTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
    }
    
    public ExamDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}