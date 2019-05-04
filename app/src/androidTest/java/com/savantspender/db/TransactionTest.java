package com.savantspender.db;

import android.database.sqlite.SQLiteConstraintException;

import com.savantspender.db.dao.AccountDao;
import com.savantspender.db.dao.TransactionDao;
import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;
import com.savantspender.db.entity.TransactionEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class TransactionTest extends DefaultDatabaseTest {
    private TransactionDao mTransactions;

    private static final String AccountId = "TestAccountId";
    private static final String ItemId = "TestItemId";
    private static final String TransId = "abc123";
    private static final Date CurrentDate = Calendar.getInstance().getTime();

    @Before
    @Override
    public void createDb() {
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

}
