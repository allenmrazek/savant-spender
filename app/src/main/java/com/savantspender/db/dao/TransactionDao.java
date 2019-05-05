package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.savantspender.db.entity.TransactionEntity;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(TransactionEntity transaction);

    @Delete
    void delete(TransactionEntity transaction);

    @Insert
    void insert(List<TransactionEntity> transactions);
}

