package com.savantspender.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.TransactionEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(TransactionEntity transaction);

    @Delete
    void delete(TransactionEntity transaction);

    @Query("SELECT * FROM transactions WHERE id IN" +
            "(SELECT transactionid FROM catalogged WHERE tagId = :tagId)") //where cattalogger tagid == tagid and transid is in table
    LiveData<List<TransactionEntity>> getTransactionsByTagId(int tagId);

    //@Query("")
    //List<TransactionEntity> get_this_month(Date date); ///I dont know how to do type converters but this wil likely be usefull

    //find by date

    //find by goal

    //find by no tags


}
