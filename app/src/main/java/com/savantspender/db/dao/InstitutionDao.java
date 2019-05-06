package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.InstitutionEntity;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

import java.util.List;

@Dao
public interface InstitutionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InstitutionEntity institution);

    @Query("SELECT * FROM institutions WHERE id = :instid")
    LiveData<InstitutionEntity> getById(@NonNull String instid);

    @Query("SELECT * FROM institutions WHERE id = :instid")
    LiveData<InstitutionEntity> exists(@NonNull String instid);

    @Delete
    void delete(InstitutionEntity institution);
}
