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


    @Query("SELECT * FROM transactions WHERE id IN" +
            "(SELECT transactionid FROM catalogged WHERE tagId = :tagId)") //where cattalogger tagid == tagid and transid is in table
    public abstract LiveData<List<TransactionEntity>> getTransactionsByTagId(int tagId);

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM transactions WHERE id = :transId) THEN 1 ELSE 0 END")
    public abstract boolean exists(@NonNull String transId);

    //@Query("")
    //List<TransactionEntity> get_this_month(Date date); ///I dont know how to do type converters but this wil likely be usefull

    //find by date

    //find by goal

    //find by no tags



}

