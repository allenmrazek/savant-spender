package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.savantspender.db.entity.WorksOnEntity;

@Dao
public interface WorksOnDao {
    @Insert
    void insert(WorksOnEntity wo);

//    LiveData<List<ProjectEntity>> getProjectsWorkedOnBy(EmployeeEntity employee) {
//
//    }
}
