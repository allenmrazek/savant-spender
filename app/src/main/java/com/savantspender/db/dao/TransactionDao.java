package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.savantspender.db.entity.TransactionEntity;
import java.util.List;

@Dao
public abstract class TransactionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(TransactionEntity transaction);

    @Delete
    public abstract void delete(TransactionEntity transaction);

    @Update
    public abstract void update(TransactionEntity e);


    @Transaction
    public void insertOrUpdate(List<TransactionEntity> transactions) {
        for (TransactionEntity t : transactions) {
            if (exists(t.id))
                update(t);
            else insert(t);
        }
    }

    @Update
    public abstract void insert(List<TransactionEntity> transactions);


    @Query("DELETE FROM transactions")
    public abstract void deleteAll();

    @Query("SELECT * FROM transactions WHERE amount > 0 ORDER BY postDate DESC")
    public abstract LiveData<List<TransactionEntity>> getSpendingTransactions();

    @Query("SELECT * FROM transactions AS T WHERE amount > 0 AND NOT EXISTS (SELECT 1 FROM catalogged AS C WHERE T.id == C.transactionId LIMIT 1)")
    public abstract LiveData<List<TransactionEntity>> getUntaggedTransactions();

    @Query("SELECT DISTINCT T.id, T.postDate, T.amount, T.accountId, T.itemId, T.pending, T.name FROM transactions AS T INNER JOIN catalogged AS C ON T.id == C.transactionId")
    public abstract LiveData<List<TransactionEntity>> getTaggedTransactions();


    @Query("SELECT DISTINCT * FROM transactions WHERE id IN" +
            "(SELECT transactionid FROM catalogged WHERE tagId = :tagId)")
    public abstract LiveData<List<TransactionEntity>> getTransactionsByTagId(int tagId);

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM transactions WHERE id = :transId LIMIT 1) THEN 1 ELSE 0 END")
    public abstract boolean exists(@NonNull String transId);

}

