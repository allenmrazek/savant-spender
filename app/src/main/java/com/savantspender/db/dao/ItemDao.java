package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.ItemEntity;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemEntity entity);

    @Query("SELECT * FROM items")
    LiveData<List<ItemEntity>> getItems();


    @Delete
    void delete(ItemEntity entity);


    @Query("SELECT access_token FROM items WHERE id == :itemId")
    String getAccessTokenSync(@NonNull String itemId);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM items WHERE id == :itemId LIMIT 1) THEN 1 ELSE 0 END")
    boolean exists(@NonNull String itemId);
}
