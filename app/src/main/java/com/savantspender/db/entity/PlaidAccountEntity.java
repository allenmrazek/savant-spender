package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "accounts", primaryKeys = { "id", "institutionId"})
public class PlaidAccountEntity {
    public @NonNull String id;               // account id
    public @NonNull String institutionId;    // + instid -> unique

    public String mask;
    public String name;
}
