package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.savantspender.db.entity.TagEntity;

import java.util.List;

@Dao
public abstract class TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(TagEntity tag);

    @Delete
    public abstract void delete(TagEntity tag);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(TagEntity tag);


    public void upsert(TagEntity tag) {
        if (exists(tag.getId())) {
            update(tag);
            return;
        } else {
            insert(tag);
        }
    }

    @Query("SELECT 1 FROM tags WHERE id = :tagId")
    public abstract boolean exists(int tagId);

    @Query("SELECT 1 FROM tags WHERE name = :tagName")
    public abstract boolean existss(@NonNull String tagName);

    @Query("SELECT * FROM tags")
    public abstract LiveData<List<TagEntity>> getTags();

    @Query("SELECT * FROM tags")
    public abstract List<TagEntity> getTagsSync();

    @Query("SELECT * FROM tags")
    public abstract List<TagEntity> getAll();

//    //returns tags which arent used in any goal
//    @Query("SELECT * FROM tags WHERE id NOT IN " +
//            "(SELECT tagId FROM GoalTagsEntity)")
//    public abstract LiveData<List<TagEntity>> getTagsNotInGoals();

//    //retunrs tags not used by any transaction
//    @Query("SELECT * FROM tags WHERE id NOT IN " +
//            "(SELECT tagId FROM catalogged)")
//    public abstract LiveData<List<TagEntity>> getTagNotInTransactions();
}
