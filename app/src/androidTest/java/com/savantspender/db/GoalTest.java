package com.savantspender.db;

import com.savantspender.LiveDataTestUtil;
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

public class GoalTest extends DefaultDatabaseTest {
    public GoalDao goalDao;
    public GoalEntity inputE0 = new GoalEntity("goa0",1.00);
    public GoalEntity inputE1 = new GoalEntity("goa1",2.00);
    public int tagId0 = 0;
    public int tagId1 = 1;

    @Before
    @Override
    public void createDb(){
        super.createDb();
        //inicalizing DOAs
        GoalTagTrackerDao goalTagTrackerDao = mDatabase.goalTagTrackerDoa();
        TagDao tagDao = mDatabase.tagDao();
        this.goalDao = mDatabase.goalDoa();

        //inicalizing entities
        GoalTagTrackerEntity goalTagTrackerE0 = new GoalTagTrackerEntity(this.inputE0.name,this.tagId0);
        GoalTagTrackerEntity goalTagTrackerE1 = new GoalTagTrackerEntity(this.inputE1.name,this.tagId1);
        TagEntity tagE0 = new TagEntity(this.tagId0,"tag");
        TagEntity tagE1 = new TagEntity(this.tagId1,"tag1");

        //insertions
        tagDao.insert(tagE0);
        tagDao.insert(tagE1);
        this.goalDao.insert(this.inputE0);
        this.goalDao.insert(this.inputE1);
        goalTagTrackerDao.insert(goalTagTrackerE0);
        goalTagTrackerDao.insert(goalTagTrackerE1);

    }

    @Test
    public void insert(){}

    @Test
    public void delete() throws InterruptedException
    {
        this.goalDao.delete(this.inputE1);
        List<GoalEntity> outputEs = LiveDataTestUtil.getValue(this.goalDao.getAll());
        GoalEntity outputE = outputEs.get(0);
        assertThat(outputEs.size(),is(equalTo(1)));
        assertThat(outputE,is(instanceOf(GoalEntity.class)));
    }

    @Test
    public void getTags4Goal() throws InterruptedException
    {
        List<TagEntity> outputEs = LiveDataTestUtil.getValue(this.goalDao.getTags4Goal(this.inputE0.name));
        TagEntity outputE = outputEs.get(0);
        assertThat(outputE,is(instanceOf(TagEntity.class)));
        assertThat(outputE.getId(),is(equalTo(this.tagId0)));
    }


}
