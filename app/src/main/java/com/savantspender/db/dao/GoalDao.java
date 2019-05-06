package com.savantspender.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.TagEntity;

import java.util.List;

@Dao
public interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(GoalEntity entity);

    @Delete
    void delete(GoalEntity entity);

    @Query("SELECT * FROM goals")
    LiveData<List<GoalEntity>> getAll();

//    @Query("SELECT * FROM tags WHERE id IN (SELECT tagId FROM go WHERE goalId = :goal_name)")
//    LiveData<List<TagEntity>> getTags4Goal(String goal_name);



}
