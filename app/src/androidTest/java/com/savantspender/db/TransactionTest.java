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
    public void getTransactionsByTagId() throws InterruptedException
    {
        //populate database to test...
        //prepopulate database
        //generating DOA links
        TagDao tagDao = mDatabase.tagDao();
        InstitutionDao institutionDao = mDatabase.institutionDao();
        ItemDao itemDao = mDatabase.itemDao();
        AccountDao accountDoa = mDatabase.accountDao();;
        CataloggedDao cataloggedDao = mDatabase.cataloggedDao();

        //generating group 1 entities
        CataloggedEntity cataloggedE0 = new CataloggedEntity("account","transaction","item",0);
        CataloggedEntity cataloggedE1 = new CataloggedEntity("account1", "transaction1","item1",1);

        TagEntity tagE0 = new TagEntity(cataloggedE0.tagId,"tag");
        InstitutionEntity institutionE0 = new InstitutionEntity("insitution","Bank");
        ItemEntity itemE0 = new ItemEntity(cataloggedE0.itemId,"insitution","access_token");
        AccountEntity accountE0 = new AccountEntity(cataloggedE0.accountId,cataloggedE0.itemId,"acount_name");
        Date date0 = new Date();
        TransactionEntity transactionE0 = new TransactionEntity(cataloggedE0.transactionId,cataloggedE0.accountId,cataloggedE0.itemId,"transaction",1.00,false,date0);

        //generating group 2 entities
        TagEntity tagE1 = new TagEntity(cataloggedE1.tagId,"tag1");
        InstitutionEntity institutionE1 = new InstitutionEntity("insitution1","Bank1");
        ItemEntity itemE1 = new ItemEntity(cataloggedE1.itemId,"insitution1","access_token1");
        AccountEntity accountE1 = new AccountEntity(cataloggedE1.accountId,cataloggedE1.itemId,"acount_name1");
        Date date1 = new Date();
        TransactionEntity transactionE1 = new TransactionEntity(cataloggedE1.transactionId,cataloggedE1.accountId,cataloggedE1.itemId,"transaction1",2.00,false,date0);

        //inserting items in database
        tagDao.insert(tagE0);
        tagDao.insert(tagE1);
        institutionDao.insert(institutionE0);
        institutionDao.insert(institutionE1);
        itemDao.insert(itemE0);
        itemDao.insert(itemE1);
        accountDoa.insert(accountE0);
        accountDoa.insert(accountE1);
        mTransactions.insert(transactionE0);
        mTransactions.insert(transactionE1);
        cataloggedDao.insert(cataloggedE0);
        cataloggedDao.insert(cataloggedE1);
        /////////////////////////////done populating
        
        
        List<TransactionEntity> outputEs = LiveDataTestUtil.getValue(mTransactions.getTransactionsByTagId(0));
        TransactionEntity outputE = outputEs.get(0);
        assertThat(outputE,is(instanceOf(TransactionEntity.class)));
        assertThat(outputEs.size(),equalTo(1));
        assertThat(outputE.id,equalTo(transactionE0.id));
        assertThat(outputE.accountId,equalTo(transactionE0.accountId));
        assertThat(outputE.itemId,equalTo(transactionE0.itemId));
        assertThat(outputE.amount,equalTo(transactionE0.amount));
        assertThat(outputE.name,equalTo(transactionE0.name));
        assertThat(outputE.pending,equalTo(transactionE0.pending));
        assertThat(outputE.postDate,equalTo(transactionE0.postDate));
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
