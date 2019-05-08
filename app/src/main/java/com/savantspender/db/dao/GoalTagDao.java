package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.savantspender.db.entity.GoalTagsEntity;

import java.util.List;
import java.util.Set;


@Dao
public abstract class GoalTagDao {

    @Insert
    public abstract void insert(GoalTagsEntity entity);

    @Delete
    public abstract void delete(GoalTagsEntity entity);

    @Query("SELECT * FROM goaltags")
    public abstract List<GoalTagsEntity> getAll();

    @Query("SELECT * FROM goaltags WHERE goalId == :goalId")
    public abstract Set<GoalTagsEntity> getTagsFor(int goalId);
}
