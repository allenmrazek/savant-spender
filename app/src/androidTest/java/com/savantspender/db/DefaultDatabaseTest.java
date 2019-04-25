package com.savantspender.db;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.savantspender.db.dao.EmployeeDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class DefaultDatabaseTest {
    protected AppDatabase mDatabase;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();

        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }


    @After
    public void closeDb() throws IOException {
        mDatabase.close();
    }
}
