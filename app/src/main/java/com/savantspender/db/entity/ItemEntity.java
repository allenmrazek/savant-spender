package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "items",
        foreignKeys = @ForeignKey(
                entity = InstitutionEntity.class,
                parentColumns = "id",
                childColumns = "institutionId"),
        indices = @Index("institutionId")
)
public class ItemEntity {
    @PrimaryKey
    @NonNull
    public String id; // itemId
    public String institutionId;
    public String access_token;
    public String public_token;

    public ItemEntity(@NonNull String id, String institutionId, String access_token, String public_token) {
        this.id = id;
        this.institutionId = institutionId;
        this.access_token = access_token;
        this.public_token = public_token;
    }
}
