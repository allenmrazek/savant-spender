package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
 tableName = "goaltagtacker",
         foreignKeys = {
@ForeignKey(
        parentColumns = "name",
        childColumns = "goalId",
        entity = GoalEntity.class,
        onDelete = CASCADE),
@ForeignKey(
        parentColumns = "id",
        childColumns = "tagId",
        entity = TagEntity.class)
        },
        indices = { @Index({"goalId"}), @Index("tagId")},
        primaryKeys = {"goalId", "tagId"}
)
public class GoalTagTrackerEntity {
    @NonNull
    public String goalId;
    @NonNull
    public int tagId;

    public GoalTagTrackerEntity(@NonNull String goalId,@NonNull int tagId)
    {
        this.goalId = goalId;
        this.tagId = tagId;
    }
}
