package com.savantspender.db.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
 tableName = "goaltags",
         foreignKeys = {
            @ForeignKey(
                    parentColumns = "id",
                    childColumns = "goalId",
                    entity = GoalEntity.class,
                    onDelete = CASCADE,
                    onUpdate = CASCADE),

            @ForeignKey(
                    parentColumns = "id",
                    childColumns = "tagId",
                    entity = TagEntity.class)},
                    indices = {
                        @Index({"goalId"}), @Index("tagId")
            },

        primaryKeys = {"goalId", "tagId"}
)
public class GoalTagsEntity {
    public int goalId;

    @NonNull
    public int tagId;

    public GoalTagsEntity(int goalId, int tagId) {
        this.goalId = goalId;
        this.tagId = tagId;
    }
}
