package com.savantspender.db.entity;

// Transactions table: {AccountID, TransactionID} -> Name, Amount, Pending, PostDate

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

import java.util.Date;

@Entity (
    primaryKeys = {
        "AccountID", "TransactionID"
    },
    tableName = "transactions",
    indices = {
        @Index("TransactionId")
    })
public class TransactionEntity {
    public @NonNull String AccountID;
    public @NonNull String TransactionID;

    public String Name;
    public float Amount;
    public boolean Pending;
    public Date PostDate;
}
