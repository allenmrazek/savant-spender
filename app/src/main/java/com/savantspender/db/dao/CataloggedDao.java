package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.TransactionEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public abstract class CataloggedDao {
    @Insert
    public abstract void insert(CataloggedEntity centity);

    @Delete
    public abstract void delete(CataloggedEntity centity);

    @Insert
    public void insert(List<TransactionEntity> transactions, int tagId) {
        CataloggedEntity ce = new CataloggedEntity("", "", "",0);

        for (TransactionEntity t : transactions) {
            ce.accountId = t.accountId;
            ce.itemId = t.itemId;
            ce.transactionId = t.id;
            ce.tagId = tagId;

            insert(ce);
        }
    }

    @Query("SELECT * FROM catalogged")
    public abstract List<CataloggedEntity> getAll();

    @Query("DELETE FROM catalogged WHERE accountId == :accountId AND transactionId == :transactionId AND itemId == :itemId")
    public abstract void untag(@NonNull String accountId, @NonNull String transactionId, @NonNull String itemId);


    public boolean hasTags(TransactionEntity transaction) {
        return hasTags(transaction.accountId, transaction.id, transaction.itemId);
    }

    @Query("SELECT 1 FROM catalogged WHERE accountId == :accountId AND transactionId == :transactionId AND itemId == :itemId LIMIT 1")
    protected abstract boolean hasTags(@NonNull String accountId, @NonNull String transactionId, @NonNull String itemId);

    @Insert
    public void untag(List<TransactionEntity> transactions) {

        for (TransactionEntity t : transactions) {
            untag(t.accountId, t.id, t.itemId);
        }
    }
}
