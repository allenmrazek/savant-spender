package com.savantspender.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.TagEntity;

import java.util.List;

@Dao
public interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TagEntity tag);

    @Delete
    void delete(TagEntity tag);


    @Query("SELECT 1 FROM tags WHERE id = :tagId")
    boolean exists(int tagId);

    @Query("SELECT * FROM tags")
    LiveData<List<TagEntity>> getTags();

    @Query("SELECT * FROM tags")
    List<TagEntity> getTagsSync();

    @Query("SELECT * FROM tags")
    List<TagEntity> getAll();

    //returns tags which arent used in any goal
    @Query("SELECT * FROM tags WHERE id NOT IN " +
            "(SELECT tagId FROM goaltagtacker)")
    LiveData<List<TagEntity>> getTagsNotInGoals();

    //retunrs tags not used by any transaction
    @Query("SELECT * FROM tags WHERE id NOT IN " +
            "(SELECT tagId FROM catalogged)")
    LiveData<List<TagEntity>> getTagNotInTransactions();
}
