package com.savantspender.db.entity;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = insitution_entity.class, parentColumns = "id",childColumns = "insitutionid"))
public class item_enity {
    @PrimaryKey
    protected String id;
    protected String access_token;
    protected String public_token;

    public String get_id() {return this.id;}
    public String get_public_token() {return this.access_token;}
    public String get_acess_toekn() {return this.public_token;}
}
