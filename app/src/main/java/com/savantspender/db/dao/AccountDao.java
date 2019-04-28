package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.savantspender.db.entity.AccountEntity;

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccountEntity entity);

    @Delete
    void delete(AccountEntity entity);
}
