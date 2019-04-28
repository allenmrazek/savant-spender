package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Accounts table: {InstitutionID, AccountID} -> account_name

@Entity(
        primaryKeys = {
                "instId",
                "accountId"
        },
        foreignKeys = @ForeignKey(
                entity = InstitutionEntity.class,
                parentColumns = "id",
                childColumns = "id")
)
public class AccountEntity {
    protected @NonNull String instId;
    protected @NonNull String accountId;
    protected @NonNull String accountName;

    public AccountEntity(@NonNull String instId, @NonNull String accountId, @NonNull String accountName) {
        this.instId = instId;
        this.accountId = accountId;
        this.accountName = accountName;
    }

    @NonNull
    public String getInstId() {
        return instId;
    }

    @NonNull
    public String getAccountId() {
        return accountId;
    }

    @NonNull
    public String getAccountName() {
        return accountName;
    }
}
