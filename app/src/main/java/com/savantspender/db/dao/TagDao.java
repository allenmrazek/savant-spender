package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.savantspender.db.entity.TagEntity;

@Dao
public interface TagDao {
    @Insert
    void insert(TagEntity tag);

    @Delete
    void delete(TagEntity tag);

    @Query("SELECT 1 FROM tags WHERE id = :tagId")
    boolean exists(int tagId);
}
