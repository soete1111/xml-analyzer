package com.example.xml_analyzer.model;

import java.time.LocalDateTime;

public class AnalysisResponse {
    private LocalDateTime analyseDate;
    private String fileName;
    private Details details;

    public LocalDateTime getAnalyseDate() {
        return analyseDate;
    }

    public void setAnalyseDate(LocalDateTime analyseDate) {
        this.analyseDate = analyseDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }
} 