package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.savantspender.db.entity.GoalTagsEntity;

import java.util.List;


@Dao
public interface GoalTagDao {

    @Insert
    void insert(GoalTagsEntity entity);

    @Delete
    void delete(GoalTagsEntity entity);

    @Query("SELECT * FROM goaltags")
    List<GoalTagsEntity> getAll();

}
