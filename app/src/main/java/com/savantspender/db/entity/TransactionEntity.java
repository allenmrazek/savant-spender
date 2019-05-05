package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import java.util.Date;

@Entity (
    primaryKeys = {
        "accountId", "id", "itemId"
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
    public double amount;
    public boolean pending;

    public TransactionEntity(
            @NonNull String id,
            @NonNull String accountId,
            @NonNull String itemId,
            @Nullable String name,
            double amount,
            boolean pending,
            @NonNull Date postDate) {

        this.accountId = accountId;
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.pending = pending;
        this.postDate = postDate;
        this.itemId = itemId;
    }

    @NonNull
    public Date postDate;

    @NonNull
    public String itemId;
}
