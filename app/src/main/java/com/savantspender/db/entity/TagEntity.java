package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.TreeMap;

@Entity(tableName = "tags")
public class TagEntity {
    @PrimaryKey
    int id;

    @NonNull
    protected String name;

    public TagEntity(int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }
}

