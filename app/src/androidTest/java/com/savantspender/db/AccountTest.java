package com.savantspender.db;

import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.AccountDao;
import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AccountTest extends DefaultDatabaseTest {
    private AccountDao mAccounts;

    private static final String AccountId = "TestAccountId";
    private static final String ItemId = "TestItemId";
    private static final String InstitutionId = "TestInstId";
    private static final String AccountName = "TestName";

    @Before
    @Override
    public void createDb(){
        super.createDb();

        mDatabase.institutionDao().insert(new InstitutionEntity(InstitutionId, "test institution"));
        mDatabase.itemDao().insert(new ItemEntity(ItemId, InstitutionId, "access-11"));
        mDatabase.accountDao().insert(new AccountEntity(AccountId, ItemId, AccountName));
        mAccounts = mDatabase.accountDao();
    }

    @Test
    public void insert_account() {
        AccountEntity testAccount = new AccountEntity("unique_id", ItemId, "test account name");

        mAccounts.insert(testAccount);
    }

    @Test
    public void delete_account() throws InterruptedException {
        assertThat(mAccounts.deleteById(AccountId), equalTo(1));
    }


    @Test
    public void retrieve_account() throws InterruptedException {
        List<AccountEntity> accounts = LiveDataTestUtil.getValue(mAccounts.getAccounts(ItemId));

        assertThat(accounts.size(), is(1));

        AccountEntity e = accounts.get(0);

        assertThat(e.id, equalTo(AccountId));
        assertThat(e.itemId, equalTo(ItemId));
        assertThat(e.name, equalTo(AccountName));
    }
}
