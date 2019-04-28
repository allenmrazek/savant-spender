package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "projects")
public class ProjectEntity {
    @PrimaryKey
    public int Pnumber;

    @NonNull
    public String Pname;


    public ProjectEntity(@NonNull String Pname, int Pnumber) {
        this.Pname = Pname;
        this.Pnumber = Pnumber;
    }
}
