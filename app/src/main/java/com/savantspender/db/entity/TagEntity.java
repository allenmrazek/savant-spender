package com.savantspender.db.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.TreeMap;

@Entity(tableName = "tags")
public class TagEntity {
    @PrimaryKey
    String value;

    public void Tag(String value)
    {
        this.value = value;
    }
    public String get_value(){return this.value;}

}

