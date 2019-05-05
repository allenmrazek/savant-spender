package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.savantspender.db.entity.GoalTagTrackerEntity;

import java.util.List;


@Dao
public interface GoalTagTrackerDao {

    @Insert
    void insert(GoalTagTrackerEntity entity);

    @Delete
    void delete(GoalTagTrackerEntity entity);

    @Query("SELECT * FROM goaltagtacker")
    List<GoalTagTrackerEntity> getAll();

}
