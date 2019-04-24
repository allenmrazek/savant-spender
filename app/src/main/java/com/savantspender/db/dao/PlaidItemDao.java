package com.savantspender.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.savantspender.db.entity.PlaidItemEntity;

import java.util.List;

@Dao
public interface PlaidItemDao {
   @Query("SELECT * FROM plaidItems")
   LiveData<List<PlaidItemEntity>> loadAccounts();

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   void insertAll(List<PlaidItemEntity> items);
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
