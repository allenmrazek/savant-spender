package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.savantspender.model.PlaidAccount;
import com.savantspender.model.PlaidItem;

import java.util.List;

@Entity(tableName = "plaidItems"
    /* foreign keys here -- todo */)
public class PlaidItemEntity implements PlaidItem {
    @PrimaryKey
    @NonNull
    private String mId;

    @NonNull
    private String mPublicToken;

    @NonNull
    private String mAccessToken;


    @Override
    public String getPublicToken() {
        return mPublicToken;
    }

    public void setPublicToken(String token) {
        mPublicToken = token;
    }

    @Override
    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String token) {
        mAccessToken = token;
    }

    @Override
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public PlaidItemEntity() {

    }

    @Ignore
    public PlaidItemEntity(String itemId, String publicToken, String accessToken) {
        mId = itemId;
        mPublicToken = publicToken;
        mAccessToken = accessToken;
    }
}
