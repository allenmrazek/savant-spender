package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.TransactionEntity;

import java.util.Date;
import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;

@Dao
public abstract class CataloggedDao {
    @Insert
    public abstract void insert(CataloggedEntity centity);

    @Delete
    public abstract void delete(CataloggedEntity centity);

    @Insert
    public void insert(List<TransactionEntity> transactions, int tagId) {
        CataloggedEntity ce = new CataloggedEntity("", "", "", 0);

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


    @Query("SELECT * FROM transactions AS T WHERE amount > 0 AND postDate BETWEEN :start AND :end AND EXISTS ("
            + "SELECT 1 FROM catalogged AS C WHERE C.accountId == T.accountId AND C.transactionId == T.id"
            + " AND C.tagId IN (:tags) LIMIT 1)")
    public abstract List<TransactionEntity> getTransactions(@NonNull Date start, @NonNull Date end, Set<Integer> tags);
}