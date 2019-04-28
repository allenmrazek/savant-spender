package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.PlaidItemEntity;

import java.util.List;

@Dao
public interface PlaidItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaidItemEntity entity);

    @Query("SELECT * FROM items")
    LiveData<List<PlaidItemEntity>> getItems();

    @Query("SELECT * FROM items WHERE inst_id == :instid")
    LiveData<List<PlaidItemEntity>> getItemsFromInstitution(@NonNull String instid);

    @Delete
    void delete(PlaidItemEntity entity);
}
