package com.savantspender.db.entity;

public interface Goal {
    String getName();
    double getAmount();
    double getPredicted();
    double getRsquared();
    double getR();
    int getProgress();
}
