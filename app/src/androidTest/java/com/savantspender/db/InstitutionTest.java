package com.savantspender.db;


import com.plaid.client.response.Institution;
import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.InstitutionDao;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;


import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class InstitutionTest extends DefaultDatabaseTest {
    private InstitutionDao DOAInstitution;
    public InstitutionEntity inputE = new InstitutionEntity("123456", "BofA");
    public InstitutionEntity inputE2 = new InstitutionEntity("234567", "Chase");

    @Before
    @Override
    public void createDb() throws InterruptedException{
        super.createDb();
        DOAInstitution = mDatabase.institutionDao();
        //declare dumby varibles
        //populate database
        DOAInstitution.insert(this.inputE);
        DOAInstitution.insert(this.inputE2);
    }

    @Test
    public void getById() throws InterruptedException
    {
        //testing the getbyid method of the doa
        InstitutionEntity outputE = LiveDataTestUtil.getValue(DOAInstitution.getById(this.inputE.id));
        assertThat(outputE, is(instanceOf(InstitutionEntity.class)));
        assertThat(outputE.id, is(equalTo(this.inputE.id)));
        assertThat(outputE.name, is(equalTo(this.inputE.name)));
    }

    @Test
    public void test_delete() throws InterruptedException {

        //testing delete
        this.DOAInstitution.delete(this.inputE);
        InstitutionEntity nullE = LiveDataTestUtil.getValue(this.DOAInstitution.getById(this.inputE.id));
        assertThat(nullE, not(instanceOf(InstitutionEntity.class))); //checks if it returned and empty value

    }



}
