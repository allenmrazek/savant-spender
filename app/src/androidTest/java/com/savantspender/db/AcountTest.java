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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class AcountTest extends DefaultDatabaseTest {
    private InstitutionDao DOAInstitution;
    @Test
    public void test_insert_read_delete() throws InterruptedException {
        //populate nessarcy items in database
        //declare dumby varibles
        String IDInstitution = "123456";
        String ID2Institution = "234567";
        InstitutionEntity inputE = new InstitutionEntity(IDInstitution, "BofA");
        InstitutionEntity expectedE = inputE;
        InstitutionEntity inputE2 = new InstitutionEntity(ID2Institution, "Chase");
        InstitutionEntity expectedE2 = inputE2;
        //prepopulate database
        DOAInstitution.insert(inputE);
        DOAInstitution.insert(inputE2);
        ////////////////////////////////////////

        String ID = "654321";
        String ID2 = "abcdefg";

        AccountEntity inputTestE = new AccountEntity(ID,IDInstitution,"main");
        AccountEntity inputTestE2 = new AccountEntity(ID2,ID2Institution,"savings");
        DOAAccount.insert(inputTestE);
        DOAAccount.insert(inputTestE2);

        //testing functionality of getAccounts
        List<AccountEntity> outputEs = DOAAccount.getAccounts();

        assertThat(outputEs.size(), is(2));

        //testing spesific getAcount
        AccountEntity outputE = DOAAccount.getAcount(ID,IDInstitution);
        assertThat(outputE.id, equalTo(ID));
        assertThat(outputE.institutionId, equalTo(IDInstitution));
        assertThat(outputE.name, equalTo("main"));


        //testing conflict resolution
        AccountEntity inputTestEALT = new AccountEntity(ID,IDInstitution,"mainALT");
        DOAAccount.insert(inputTestEALT);
        AccountEntity outputEALT = DOAAccount.getAcount(ID,IDInstitution);
        assertThat(outputEALT.id, equalTo(ID));
        assertThat(outputEALT.institutionId, equalTo(IDInstitution));
        assertThat(outputEALT.name, equalTo("mainALT"));


        //testing delete
        DOAAccount.delete(inputTestEALT);
        List<AccountEntity> outputEs2 = DOAAccount.getAccounts();
        assertThat(outputEs2.size(), is(1));

    }

    private AccountDao DOAAccount;

    @Before
    @Override
    public void createDb() {
        super.createDb();
        DOAAccount = mDatabase.accountDao();
        DOAInstitution = mDatabase.institutionDao();
    }
}