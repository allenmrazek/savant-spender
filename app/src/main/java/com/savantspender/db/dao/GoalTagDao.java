package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.savantspender.db.entity.GoalTagsEntity;
import com.savantspender.db.entity.Tag;

import java.util.List;


@Dao
public abstract class GoalTagDao {

    @Insert
    public abstract void insert(GoalTagsEntity entity);

    @Delete
    public abstract void delete(GoalTagsEntity entity);

    @Query("SELECT * FROM goaltags")
    public abstract List<GoalTagsEntity> getAll();
}
