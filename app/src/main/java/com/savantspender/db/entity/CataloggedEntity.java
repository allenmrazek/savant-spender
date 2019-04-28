package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "catalogged",
        foreignKeys = {
                @ForeignKey(
                        parentColumns = "AccountId",
                        childColumns = "id",
                        entity = AccountEntity.class),
                @ForeignKey(
                        parentColumns = "TransactionId",
                        childColumns = "id",
                        entity = TransactionEntity.class),
                @ForeignKey(
                        parentColumns = "TagId",
                        childColumns = "id",
                        entity = TagEntity.class
                )
        },
        indices = { @Index("TransactionId")},
        primaryKeys = {"AccountId", "TransactionId", "TagId"})
public class CataloggedEntity {
    public @NonNull String AccountId;
    public @NonNull String TransactionId;
    public int TagId;
}
