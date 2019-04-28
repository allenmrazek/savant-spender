package com.savantspender.db.entity;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "institutions",
        foreignKeys = @ForeignKey(
                entity = InstitutionEntity.class,
                parentColumns = "id",
                childColumns = "id"))
public class ItemEntity {
    @PrimaryKey
    protected String id;
    protected String access_token;
    protected String public_token;

    public String getId() {return this.id;}
    public String getPublicToken() {return this.access_token;}
    public String getAccessToken() {return this.public_token;}
}
