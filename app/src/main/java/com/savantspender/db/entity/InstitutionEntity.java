package com.savantspender.db.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InstitutionEntity {
    @PrimaryKey
    protected String id;
    protected String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
