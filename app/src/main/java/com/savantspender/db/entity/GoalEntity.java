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
public class GoalEntity implements Goal {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    public double amount;
    public double predicted;
    public double rsquared;
    public double rvalue;
    public int progress;

    @Ignore
    public GoalEntity(@NonNull String name, double amount) {
        this.name = name;
        this.amount = amount;
        this.id = 0;
        predicted = 0.0;
        rsquared = 0.0;
        rvalue = 0.0;
    }

    public GoalEntity(int id, @NonNull String name, double amount, double predicted, double rsquared, double rvalue, int progress) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.predicted = predicted;
        this.rsquared = rsquared;
        this.rvalue = rvalue;
        this.progress = progress;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public double getPredicted() {
        return predicted;
    }

    @Override
    public double getRsquared() {
        return rsquared;
    }

    @Override
    public double getR() {
        return rvalue;
    }

    @Override
    public int getProgress() { return progress; }
}
