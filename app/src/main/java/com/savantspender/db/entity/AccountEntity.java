package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Accounts table: {InstitutionID, AccountID} -> account_name

@Entity(
        tableName = "accounts",
        primaryKeys = {
                "institutionId",
                "id"
        },
        foreignKeys = @ForeignKey(
                entity = InstitutionEntity.class,
                parentColumns = "id",
                childColumns = "institutionId")
)
public class AccountEntity {
    public @NonNull String institutionId;
    public @NonNull String id;
    public @NonNull String name;
}
