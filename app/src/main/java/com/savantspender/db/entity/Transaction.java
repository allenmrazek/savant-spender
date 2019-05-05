package com.savantspender.db.entity;

import androidx.annotation.NonNull;

import java.util.Date;

public interface Transaction {
    @NonNull String getName();
    @NonNull Date getDate();
    @NonNull double getAmount();
}
