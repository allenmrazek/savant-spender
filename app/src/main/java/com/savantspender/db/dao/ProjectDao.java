package com.savantspender.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.ProjectEntity;

@Dao
public interface ProjectDao {
    @Query("SELECT Pname FROM projects WHERE Pnumber = :pnumber")
    LiveData<String> getPnameByPnumber(int pnumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProjectEntity projectEntity);
}
