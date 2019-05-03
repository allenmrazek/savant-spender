package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.TreeMap;

@Entity(tableName = "tags",
    indices = { @Index("id"), @Index(value = "name", unique = true) })
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

