package com.savantspender.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.savantspender.db.entity.InstitutionEntity;

@Dao
public interface InstitutionDao {
    @Insert
    void insert(InstitutionEntity institution);
}
