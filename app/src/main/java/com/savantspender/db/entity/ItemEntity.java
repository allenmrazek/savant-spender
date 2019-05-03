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
    @PrimaryKey @NonNull
    public String id; // itemId

    @NonNull
    public String institutionId;

    @NonNull
    public String access_token;

    public ItemEntity(@NonNull String id, @NonNull String institutionId, @NonNull String access_token) {
        this.id = id;
        this.institutionId = institutionId;
        this.access_token = access_token;
    }
}
