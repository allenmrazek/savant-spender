package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import java.util.Date;

@Entity (
    primaryKeys = {
        "accountId", "id"
    },
    tableName = "transactions",
        foreignKeys = {
            @ForeignKey(
                    parentColumns = { "id", "itemId" },
                    childColumns = { "accountId", "itemId" },
                    entity = AccountEntity.class)
        },
    indices = {
        @Index({"accountId", "itemId"})
    })
public class TransactionEntity {
    public @NonNull String accountId;
    public @NonNull String id;

    @Nullable
    public String name;
    public float amount;
    public boolean pending;

    @NonNull
    public Date postDate;

    @NonNull
    public String itemId;
}
