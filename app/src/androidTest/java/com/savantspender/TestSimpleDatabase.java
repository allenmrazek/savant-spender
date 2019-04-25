package com.savantspender;

import android.content.Context;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.savantspender.db.AppDatabase;
import com.savantspender.db.dao.EmployeeDao;
import com.savantspender.db.entity.EmployeeEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TestSimpleDatabase {
    private AppDatabase mDatabase;
    private EmployeeDao mEmpDao;
    private SimpleDateFormat mFormatter;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();

        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mEmpDao = mDatabase.employeeDao();
        mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    }


    @After
    public void closeDb() throws IOException {
        mDatabase.close();
    }


    @Test
    public void insertEmployeeAndReadEmployee() throws Exception {
        // 'James', 'E', 'Borg', '888665555', '1937-11-10', '450 Stone, Houston, TX', 'M', 55000, NULL, 1),
        EmployeeEntity emp = new EmployeeEntity("888665555", "James", "Borg", mFormatter.parse("1937-11-10"), 1);


        mEmpDao.insert(emp);

        List<EmployeeEntity> employeeEntities = LiveDataTestUtil.getValue(mEmpDao.loadEmployees());

        assertThat(employeeEntities.size(), is(1));

        EmployeeEntity back = employeeEntities.get(0);

        assertThat(back.Ssn, equalTo(emp.Ssn));
    }
}
