package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
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
public class TransactionEntity implements Transaction {
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

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public Date getDate() {
        return postDate;
    }

    @NonNull
    @Override
    public double getAmount() {
        return amount;
    }


    @Ignore
    public boolean selected = false;

    public void setSelected(boolean tf) {
        selected = tf;
    }

    public boolean isSelected() {
        return selected;
    }
}
