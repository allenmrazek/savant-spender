package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals",
    indices = {
        @Index(value = "name", unique = true)
    })
public class GoalEntity {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    public double amount;
    public double predicted;

    public GoalEntity(int id, @NonNull String name, double amount, double predicted) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.predicted = predicted;
    }
}
