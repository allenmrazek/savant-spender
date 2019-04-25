package com.savantspender.db;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.EmployeeDao;
import com.savantspender.db.dao.ProjectDao;
import com.savantspender.db.dao.WorksOnDao;
import com.savantspender.db.entity.EmployeeEntity;
import com.savantspender.db.entity.ProjectEntity;
import com.savantspender.db.entity.WorksOnEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EmployeeTest extends DefaultDatabaseTest {
    private EmployeeDao mEmpDao;
    private ProjectDao mProjDao;
    private WorksOnDao mWorksOnDao;

    private SimpleDateFormat mFormatter;

    @Before
    @Override
    public void createDb() {
        super.createDb();
        mEmpDao = mDatabase.employeeDao();
        mProjDao = mDatabase.projectDao();
        mWorksOnDao = mDatabase.worksOnDao();
        mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    }


    @Test
    public void insert_read_delete() throws Exception {
        // 'James', 'E', 'Borg', '888665555', '1937-11-10', '450 Stone, Houston, TX', 'M', 55000, NULL, 1),
        EmployeeEntity emp = new EmployeeEntity("888665555", "James", "Borg", mFormatter.parse("1937-11-10"), 1);

        mEmpDao.insert(emp);

        List<EmployeeEntity> employeeEntities = LiveDataTestUtil.getValue(mEmpDao.loadEmployees());
        assertThat(employeeEntities.size(), is(1));

        EmployeeEntity back = employeeEntities.get(0);
        assertThat(back.Ssn, equalTo(emp.Ssn));

        mEmpDao.delete(back);
        employeeEntities = LiveDataTestUtil.getValue(mEmpDao.loadEmployees());

        assertThat(employeeEntities.size(), is(0));
    }

    public void project_relations() throws Exception {
        EmployeeEntity emp = new EmployeeEntity("888665555", "James", "Borg", mFormatter.parse("1937-11-10"), 1);
        ProjectEntity proj = new ProjectEntity("Project 1", 1);
        WorksOnEntity wo = new WorksOnEntity(emp, proj);

        mEmpDao.insert(emp);
        mProjDao.insert(proj);
        mWorksOnDao.insert(wo);

        List<EmployeeEntity> employees = LiveDataTestUtil.getValue(mEmpDao.loadEmployees());
        assertThat(employees.size(), is(1));

        EmployeeEntity foundEmployee = employees.get(0);

        assertThat(foundEmployee.Ssn, is("888665555"));


        List<ProjectEntity> projects = LiveDataTestUtil.getValue(mEmpDao.getProjects(foundEmployee.Ssn));


        assertThat(projects.size(), is(1));

        ProjectEntity feProj = projects.get(0);

        assertThat(feProj, equalTo(proj));
    }
}
