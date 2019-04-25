package com.savantspender.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.EmployeeEntity;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM employees")
    LiveData<List<EmployeeEntity>> loadEmployees();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EmployeeEntity employee);
    // examples
//    @Query("SELECT * FROM comments where productId = :productId")
//    LiveData<List<CommentEntity>> loadComments(int productId);
//
//    @Query("SELECT * FROM comments where productId = :productId")
//    List<CommentEntity> loadCommentsSync(int productId);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<CommentEntity> comments);
}
