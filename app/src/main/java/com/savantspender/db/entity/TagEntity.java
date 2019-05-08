package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import java.util.TreeMap;

@Entity(tableName = "tags",
    indices = { @Index("id"), @Index(value = "name", unique = true) })
public class TagEntity implements Tag {
    @PrimaryKey (autoGenerate = true)
    int id;

    @NonNull
    protected String name;

    public int iconId;

    @Ignore
    public boolean selected = false;

    public TagEntity(int id, @NonNull String name, int iconId) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
    }

    public int getId() {
        return id;
    }

    @Override
    public int getIconId() {
        return iconId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public void setSelected(boolean tf) {
        selected = tf;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }
}

