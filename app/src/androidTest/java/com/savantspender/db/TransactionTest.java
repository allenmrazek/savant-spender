package com.savantspender.db;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;

import com.savantspender.LiveDataTestUtil;
import com.savantspender.db.dao.AccountDao;
import com.savantspender.db.dao.CataloggedDao;
import com.savantspender.db.dao.InstitutionDao;
import com.savantspender.db.dao.ItemDao;
import com.savantspender.db.dao.TagDao;
import com.savantspender.db.dao.TransactionDao;
import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.TransactionEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TransactionTest extends DefaultDatabaseTest {
    private TransactionDao mTransactions;

    private static final String AccountId = "TestAccountId";
    private static final String ItemId = "TestItemId";
    private static final String TransId = "abc123";
    private static final Date CurrentDate = Calendar.getInstance().getTime();


    @Before
    @Override
    public void createDb() throws InterruptedException{
        super.createDb();

        final String instid = "inst_1";
        final String instName = "inst_name";

        // create an institution (required to create items)
        mDatabase.institutionDao().insert(new InstitutionEntity(instid, instName));

        // create a new item (required to create accounts)
        mDatabase.itemDao().insert(new ItemEntity(ItemId, instid, "access_token_here"));

        // create a new account (required to create transactions)
        mDatabase.accountDao().insert(new AccountEntity(AccountId, ItemId, "account_name_here"));

        mTransactions = mDatabase.transactionDao();


    }

    @Test
    public void getTransactionsByGoal() throws InterruptedException
    {
        //prepopulate database
        super.fullpopulate(5);
        GoalEntity inputE = super.goalGenorator(0);
        List<TransactionEntity> outputEs = LiveDataTestUtil.getValue(mTransactions.getTransactionsByGoal(inputE.name));
        TransactionEntity outputE = outputEs.get(0);
        TransactionEntity expectedE = super.transactionGenorator(0);
        super.fullTransactionVerifier(outputE,expectedE);
    }

    @Test
    public void getTransactionsWOTags() throws InterruptedException
    {
        //prepopulate database
        //prepopulate database
        super.prepopulateDB("Tag",1);
        super.prepopulateDB("Institution",1);
        super.prepopulateDB("Item",1);
        super.prepopulateDB("Account",1);
        super.prepopulateDB("Transaction",1);
        super.prepopulateDB("Catalogged",1);

        //deleting a catalogger so there is a return value
        super.mDatabase.cataloggedDao().delete(super.cataloggedGenorator(1));
        super.mDatabase.tagDao().delete(super.tagGenorator(1));


        TransactionEntity expectedE0 = super.transactionGenorator(1);

        List<TransactionEntity> outputEs = LiveDataTestUtil.getValue(mTransactions.getTransactionsWOTags());
        TransactionEntity outputE = outputEs.get(0);
        assertThat(outputEs.size(),equalTo(1));
        super.fullTransactionVerifier(outputE,expectedE0);

    }

    @Test
    public void getTransactionsByTagId() throws InterruptedException
    {
        //prepopulate database
        super.prepopulateDB("Tag",1);
        super.prepopulateDB("Institution",1);
        super.prepopulateDB("Item",1);
        super.prepopulateDB("Account",1);
        super.prepopulateDB("Transaction",1);
        super.prepopulateDB("Catalogged",1);

        TransactionEntity transactionE0 = super.transactionGenorator(0);
        
        
        List<TransactionEntity> outputEs = LiveDataTestUtil.getValue(mTransactions.getTransactionsByTagId(0));
        TransactionEntity outputE = outputEs.get(0);
        assertThat(outputEs.size(),equalTo(1));
        super.fullTransactionVerifier(outputE,transactionE0);
    }

    @Test
    public void insert() {
        mTransactions.insert(new TransactionEntity(TransId, AccountId, ItemId,"trans-name", 123f, false, CurrentDate));
    }

    @Test
    public void insert_bad_item() {
        thrown.expect(SQLiteConstraintException.class);
        thrown.expectMessage("FOREIGN KEY constraint failed");

        mTransactions.insert(new TransactionEntity(TransId, AccountId, "bad item", "trans-name", 123f, false, CurrentDate));
    }

    @Test
    public void insert_bad_account() {
        thrown.expect(SQLiteConstraintException.class);
        thrown.expectMessage("FOREIGN KEY constraint failed");

        mTransactions.insert(new TransactionEntity(TransId, "badAccount", ItemId, "trans-name", 123f, false, CurrentDate));
    }

    @Test
    public void insert_bad_account_and_bad_item() {
        thrown.expect(SQLiteConstraintException.class);
        thrown.expectMessage("FOREIGN KEY constraint failed");

        mTransactions.insert(new TransactionEntity(TransId, "badAccount", "bad item", "trans-name", 123f, false, CurrentDate));
    }

    @Test
    public void insert_duplicate_trans_id() {
        thrown.expect(SQLiteConstraintException.class);
        thrown.expectMessage("UNIQUE constraint failed");

        mTransactions.insert(new TransactionEntity(TransId, AccountId, ItemId, "some transaction", 456f, false, CurrentDate));
        mTransactions.insert(new TransactionEntity(TransId, AccountId, ItemId, "different trans name", 456f, false, CurrentDate));
    }

    @Test
    public void check_exists() {
        mTransactions.insert(new TransactionEntity("id", AccountId, ItemId, "name", 0f, false, CurrentDate));

        assertThat(mTransactions.exists("id"), is(true));
    }

}
