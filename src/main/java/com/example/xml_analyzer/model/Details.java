package com.example.xml_analyzer.model;

import java.time.LocalDateTime;

public class Details {
    private LocalDateTime firstPost;
    private LocalDateTime lastPost;
    private long totalPosts;
    private long totalAcceptedPosts;
    private double avgScore;

    // Getters and setters
    public LocalDateTime getFirstPost() {
        return firstPost;
    }

    public void setFirstPost(LocalDateTime firstPost) {
        this.firstPost = firstPost;
    }

    public LocalDateTime getLastPost() {
        return lastPost;
    }

    public void setLastPost(LocalDateTime lastPost) {
        this.lastPost = lastPost;
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