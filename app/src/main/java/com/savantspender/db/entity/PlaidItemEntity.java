package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "items")
public class PlaidItemEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private @NonNull String mId;

    @ColumnInfo(name = "access_token")
    private @NonNull String mAccessToken;

    @ColumnInfo(name = "inst_id")
    private @NonNull String mInstId;

    public String getId() {
        return mId;
    }
    public String getAccessToken() {
        return mAccessToken;
    }
    public String getInstId() { return mInstId; }


    public PlaidItemEntity(@NonNull String id, @NonNull String accessToken, @NonNull String instId) {
        mId = id;
        mAccessToken = accessToken;
        mInstId = instId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;

        PlaidItemEntity ple = (PlaidItemEntity)obj;

        return mId.equals(ple.mId) && mAccessToken.equals(ple.mAccessToken) && mInstId.equals(ple.mInstId);
    }
}
