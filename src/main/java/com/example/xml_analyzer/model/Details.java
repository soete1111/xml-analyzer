package com.example.xml_analyzer.model;

import java.time.LocalDateTime;

public class Details {
    private LocalDateTime firstPostDate;
    private LocalDateTime lastPostDate;
    private long totalPosts;
    private long totalAcceptedPosts;
    private double avgScore;

    public LocalDateTime getFirstPostDate() {
        return firstPostDate;
    }

    public void setFirstPostDate(LocalDateTime firstPostDate) {
        this.firstPostDate = firstPostDate;
    }

    public LocalDateTime getLastPostDate() {
        return lastPostDate;
    }

    public void setLastPostDate(LocalDateTime lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public long getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(long totalPosts) {
        this.totalPosts = totalPosts;
    }

    public long getTotalAcceptedPosts() {
        return totalAcceptedPosts;
    }

    public void setTotalAcceptedPosts(long totalAcceptedPosts) {
        this.totalAcceptedPosts = totalAcceptedPosts;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }
}