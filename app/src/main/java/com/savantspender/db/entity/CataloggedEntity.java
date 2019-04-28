package com.savantspender.db.entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
        tableName = "catalogged",
        foreignKeys = {
                @ForeignKey(
                        parentColumns = { "id", "institutionId" },
                        childColumns = { "accountId", "institutionId" },
                        entity = AccountEntity.class),
                @ForeignKey(
                        parentColumns = { "accountId", "id" },
                        childColumns = {"accountId", "transactionId"},
                        entity = TransactionEntity.class),
                @ForeignKey(
                        parentColumns = "id",
                        childColumns = "tagId",
                        entity = TagEntity.class
                )
        },
        indices = { @Index("transactionId"), @Index({"accountId", "institutionId"}), @Index("tagId")},
        primaryKeys = {"accountId", "transactionId", "tagId"}
)
public class CataloggedEntity {
    public @NonNull String accountId;
    public @NonNull String transactionId;
    public @NonNull String institutionId;
    public int tagId;
}
