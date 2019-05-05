package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.AccountEntity;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AccountEntity entity);

    @Delete
    void delete(AccountEntity entity);

    @Query("DELETE FROM accounts WHERE id = :accountId")
    int deleteById(@NonNull String accountId);

    @Query("SELECT * FROM accounts")
    LiveData<List<AccountEntity>> getAllAccounts();

    @Query("SELECT * FROM accounts WHERE itemId = :itemId")
    LiveData<List<AccountEntity>> getAccounts(@NonNull String itemId);
}
