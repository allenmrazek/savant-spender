package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "catalogged",
        foreignKeys = {
                @ForeignKey(
                        parentColumns = {"id", "itemId"},
                        childColumns = {"accountId", "itemId"},
                        entity = AccountEntity.class,
                        onDelete = CASCADE,
                        onUpdate = CASCADE),

                @ForeignKey(
                        parentColumns = {"id", "accountId", "itemId"},
                        childColumns = {"transactionId", "accountId", "itemId"},
                        entity = TransactionEntity.class,
                        onDelete = CASCADE,
                        onUpdate = CASCADE),

                @ForeignKey(
                        parentColumns = "id",
                        childColumns = "tagId",
                        entity = TagEntity.class,
                        onDelete = CASCADE,
                        onUpdate = CASCADE)
        },
        indices = { @Index({"transactionId", "accountId", "itemId"}), @Index({"accountId", "itemId"}), @Index("tagId")},
        primaryKeys = {"accountId", "transactionId", "tagId", "itemId"}
)
public class CataloggedEntity {
    public @NonNull String accountId;
    public @NonNull String transactionId;
    public @NonNull String itemId;
    public int tagId;


    public CataloggedEntity
            (@NonNull String accountId,
             @NonNull String transactionId,
             @NonNull String itemId,
             int tagId)
    {
        this.accountId = accountId;
        this.transactionId = transactionId;
        this.itemId = itemId;
        this.tagId = tagId;

    }

}
