package com.savantspender.db.dao;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.TagEntity;

import java.util.List;

@Dao
public interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(GoalEntity entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(GoalEntity entity);

    @Delete
    void delete(GoalEntity entity);

    @Query("DELETE FROM goals")
    void deleteAll();

    @Query("SELECT * FROM goals ORDER BY name ASC")
    LiveData<List<GoalEntity>> getAll();

    @Query("SELECT id FROM goals WHERE name = :name LIMIT 1")
    int getByName(@NonNull String name);

    @Query("SELECT 1 FROM goals WHERE name == :name LIMIT 1")
    boolean exists(@NonNull String name);

//    @Query("SELECT * FROM tags WHERE id IN (SELECT tagId FROM go WHERE goalId = :goal_name)")
//    LiveData<List<TagEntity>> getTags4Goal(String goal_name);

    @Query("SELECT * FROM goals ORDER BY name ASC")
    List<GoalEntity> getAllSync();

}
