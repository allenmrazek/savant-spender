package com.savantspender.db;

import com.savantspender.db.dao.GoalDao;
import com.savantspender.db.dao.GoalTagTrackerDao;
import com.savantspender.db.dao.TagDao;
import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.GoalTagTrackerEntity;
import com.savantspender.db.entity.TagEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GoalTagTrackerTest extends DefaultDatabaseTest {
    public GoalTagTrackerDao goalTagTrackerDao;
    public GoalTagTrackerEntity inputE0 = new GoalTagTrackerEntity("goal",0);
    public GoalTagTrackerEntity inputE1 = new GoalTagTrackerEntity("goal1",1);


    @Before
    @Override
    public void createDb() {
        super.createDb();
        //inicalizing varibles
        int tagId0 = 0;
        int tagId1 = 1;

        //inicalizing DOAs
        this.goalTagTrackerDao = mDatabase.goalTagTrackerDoa();
        TagDao tagDao = mDatabase.tagDao();
        GoalDao goalDao = mDatabase.goalDoa();

        //inicalizing entities
        GoalEntity goalE0 = new GoalEntity(this.inputE0.goalId,1.00);
        GoalEntity goalE1 = new GoalEntity(this.inputE1.goalId,2.00);
        TagEntity tagE0 = new TagEntity(this.inputE0.tagId,"tag");
        TagEntity tagE1 = new TagEntity(this.inputE1.tagId,"tag1");

        //insertions
        tagDao.insert(tagE0);
        tagDao.insert(tagE1);
        goalDao.insert(goalE0);
        goalDao.insert(goalE1);
        this.goalTagTrackerDao.insert(this.inputE0);
        this.goalTagTrackerDao.insert(this.inputE1);

    }

    @Test
    public void insert(){}

    @Test
    public void delete()
    {
        this.goalTagTrackerDao.delete(this.inputE1);
        List<GoalTagTrackerEntity> outputEs = this.goalTagTrackerDao.getAll();
        GoalTagTrackerEntity outputE = outputEs.get(0);
        assertThat(outputEs.size(),is(equalTo(1)));
        assertThat(outputE,is(instanceOf(GoalTagTrackerEntity.class)));
    }
}
