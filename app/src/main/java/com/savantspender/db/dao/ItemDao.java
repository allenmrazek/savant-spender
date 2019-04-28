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

    @Query("SELECT * FROM items WHERE inst_id == :instid")
    LiveData<List<ItemEntity>> getItemsFromInstitution(@NonNull String instid);

    @Delete
    void delete(ItemEntity entity);
}
