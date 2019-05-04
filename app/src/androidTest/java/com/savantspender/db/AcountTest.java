package com.savantspender.db;

import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.AccountDao;
import com.savantspender.db.dao.InstitutionDao;
import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class AcountTest extends DefaultDatabaseTest {
    private InstitutionDao DOAInstitution;
    private String IDInstitution = "123456";
    private String ID2Institution = "234567";
    private AccountEntity inputE = new AccountEntity("654321",this.IDInstitution,"main");
    private AccountEntity inputE2 = new AccountEntity("654321",this.ID2Institution,"savings");


    @Test
    public void test_getAccounts() throws InterruptedException
    {
        //testing functionality of getAccounts
        List<AccountEntity> outputEs = LiveDataTestUtil.getValue(DOAAccount.getAllAccounts());

        assertThat(outputEs.size(), is(2));

    }
    @Test
    public void test_getAccount() throws InterruptedException, Exception
    {
        //testing spesific getAcount
//        AccountEntity outputE = DOAAccount.getAcount(this.inputE.id,this.inputE.institutionId);
//        assertThat(this.inputE, is(instanceOf(AccountEntity.class)));
//        assertThat(outputE.id, equalTo(this.inputE.id));
//        assertThat(outputE.itemId, equalTo(this.inputE.itemId));
//        assertThat(outputE.name, equalTo(this.inputE.name));
        throw new Exception("broken test");
    }

    @Test
    public void test_conflict_resolution() throws InterruptedException, Exception
    {
//        //testing conflict resolution
//        AccountEntity inputTestEALT = new AccountEntity(this.inputE.id,this.inputE.institutionId,"mainALT");
//        DOAAccount.insert(inputTestEALT);
//        AccountEntity outputEALT = DOAAccount.getAcount(this.inputE.id,this.inputE.institutionId);
//        assertThat(outputEALT.id, equalTo(this.inputE.id));
//        assertThat(outputEALT.institutionId, equalTo(this.inputE.institutionId));
//        assertThat(outputEALT.name, equalTo("mainALT"));
        throw new Exception("broken test");
    }


    @Test
    public void test_delete() throws InterruptedException {

        //testing delete
        DOAAccount.delete(this.inputE);
        List<AccountEntity> outputEs2 = LiveDataTestUtil.getValue(DOAAccount.getAllAccounts());
        assertThat(outputEs2.size(), is(1));
    }

    private AccountDao DOAAccount;

    @Before
    @Override
    public void createDb() {
        super.createDb();
        DOAAccount = mDatabase.accountDao();
        DOAInstitution = mDatabase.institutionDao();
        //populate nessarcy items in database
        //declare dumby varibles
        InstitutionEntity inputInstE = new InstitutionEntity(this.IDInstitution, "BofA");
        InstitutionEntity inputInstE2 = new InstitutionEntity(this.ID2Institution, "Chase");
        //prepopulate database
        DOAInstitution.insert(inputInstE);
        DOAInstitution.insert(inputInstE2);
        ////////////////////////////////////////
        DOAAccount.insert(this.inputE);
        DOAAccount.insert(this.inputE2);
    }
}