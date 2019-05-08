package com.savantspender.db.entity;

public interface Goal {
    String getName();
    double getGoalAmount();
    double getPredicted();
    double getRsquared();
    double getR();
    int getProgress();
    double getTotalSpending();
}
