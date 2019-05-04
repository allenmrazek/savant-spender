package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "catalogged",
        foreignKeys = {
                @ForeignKey(
                        parentColumns = {"id", "itemId"},
                        childColumns = {"accountId", "itemId"},
                        entity = AccountEntity.class),
                @ForeignKey(
                        parentColumns = {"id", "accountId", "itemId"},
                        childColumns = {"transactionId", "accountId", "itemId"},
                        entity = TransactionEntity.class),
                @ForeignKey(
                        parentColumns = "id",
                        childColumns = "tagId",
                        entity = TagEntity.class)
        },
        indices = { @Index({"transactionId", "accountId", "itemId"}), @Index({"accountId", "itemId"}), @Index("tagId")},
        primaryKeys = {"accountId", "transactionId", "tagId", "itemId"}
)
public class CataloggedEntity {
    public @NonNull String accountId;
    public @NonNull String transactionId;
    public @NonNull String itemId;
    public int tagId;
}
