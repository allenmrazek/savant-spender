package com.savantspender.db;

import org.junit.Before;
import org.junit.Test;

public class Tag extends DefaultDatabaseTest {


    @Before
    @Override
    public void createDb()
    {
        super.createDb();
        super.prepopulateDB("Tag",5);
    }

    @Test
    void getTagsNotInGoals() throws InterruptedException
    {

    }
}
