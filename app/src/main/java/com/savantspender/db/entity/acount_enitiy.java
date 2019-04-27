package com.savantspender.db.entity;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = insitution_entity, parentColumns = "id",childColumns = "insitutionid"))
public class acount_enitiy {
    @PrimaryKey
    protected String id;

    public String get_id() {return this.id;}
}
