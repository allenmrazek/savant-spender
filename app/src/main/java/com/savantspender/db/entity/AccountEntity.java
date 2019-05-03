package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "accounts",
        primaryKeys = {
                "itemId",
                "id"
        },
        foreignKeys = @ForeignKey(
                entity = ItemEntity.class,
                parentColumns = "id",
                childColumns = "itemId")
)
public class AccountEntity {
    public @NonNull String id;
    public @NonNull String itemId;
    public @NonNull String name;

    public AccountEntity(@NonNull String id, @NonNull String itemId, @NonNull String name) {
        this.id = id;
        this.itemId = itemId;
        this.name = name;
    }
}
