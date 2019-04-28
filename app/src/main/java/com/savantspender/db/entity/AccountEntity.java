package com.savantspender.db.entity;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = InstitutionEntity.class, parentColumns = "id",childColumns = "id"))
public class AccountEntity {
    @PrimaryKey
    protected String id;

    public String get_id() {return this.id;}
}
