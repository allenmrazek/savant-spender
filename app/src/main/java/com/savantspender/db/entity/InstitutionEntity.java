package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "institutions"
)
public class InstitutionEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String name;

    public InstitutionEntity(@NonNull String id, String name) {
        this.id = id;
        this.name = name;
    }
}
