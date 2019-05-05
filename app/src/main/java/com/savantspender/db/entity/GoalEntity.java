package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class GoalEntity {
    @NonNull
    @PrimaryKey
    public String name;
    public double amount;
    public GoalEntity(@NonNull String name, double amount)
    {
        this.name = name;
        this.amount = amount;
    }
}
