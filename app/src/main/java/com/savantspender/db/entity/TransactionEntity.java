package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

import java.util.Date;

@Entity (
    primaryKeys = {
        "accountId", "id"
    },
    tableName = "transactions",
    indices = {
        @Index("id")
    })
public class TransactionEntity {
    public @NonNull String accountId;
    public @NonNull String id;

    public String name;
    public float amount;
    public boolean pending;
    public Date postDate;
}
