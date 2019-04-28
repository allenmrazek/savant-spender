package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.savantspender.db.entity.PlaidAccountEntity;

@Dao
public interface PlaidAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaidAccountEntity entity);

    @Delete
    void delete(PlaidAccountEntity entity);
}
