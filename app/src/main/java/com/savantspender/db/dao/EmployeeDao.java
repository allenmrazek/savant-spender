package com.savantspender.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.EmployeeEntity;
import com.savantspender.db.entity.ProjectEntity;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM employees")
    LiveData<List<EmployeeEntity>> loadEmployees();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EmployeeEntity employee);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<EmployeeEntity> employees);

    @Query("SELECT * FROM employees WHERE Lname = :Lname")
    LiveData<EmployeeEntity> getEmployeeByLname(String Lname);

    @Delete
    void delete(EmployeeEntity emp);

    @Delete
    void delete(List<EmployeeEntity> employees);

    @Query("SELECT Pname, Pnumber FROM employees INNER JOIN works_on ON Ssn = :ssn AND Ssn = Essn INNER JOIN projects WHERE Pno = Pnumber ORDER BY Pname ASC")
    LiveData<List<ProjectEntity>> getProjects(@NonNull String ssn);

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
