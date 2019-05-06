package com.savantspender.db;


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

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CataloggerTest extends DefaultDatabaseTest {
    //uses tag, transaction and accont entites
    //object deffintions
    private CataloggedDao cataloggedDao;
    private CataloggedEntity inputE0 = new CataloggedEntity("acount","trans","item",0);
    private CataloggedEntity inputE1 = new CataloggedEntity("acount1","trans1","item1",1);


    @Before
    @Override
    public void createDb() {
        super.createDb();
        //prepopulate database
        //generating DOA links
        TagDao tagDao = mDatabase.tagDao();
        InstitutionDao institutionDao = mDatabase.institutionDao();
        ItemDao itemDao = mDatabase.itemDao();
        AccountDao accountDoa = mDatabase.accountDao();
        TransactionDao transactionDao = mDatabase.transactionDao();
        this.cataloggedDao = mDatabase.cataloggedDao();

        //generating group 1 entities
        TagEntity tagE0 = new TagEntity(this.inputE0.tagId,"tag");
        InstitutionEntity institutionE0 = new InstitutionEntity("insitution","Bank");
        ItemEntity itemE0 = new ItemEntity(this.inputE0.itemId,"insitution","access_token");
        AccountEntity accountE0 = new AccountEntity(this.inputE0.accountId,this.inputE0.itemId,"acount_name");
        Date date0 = new Date();
        TransactionEntity transactionE0 = new TransactionEntity(this.inputE0.transactionId,this.inputE0.accountId,this.inputE0.itemId,"transaction",1.00,false,date0);

        //generating group 2 entities
        TagEntity tagE1 = new TagEntity(this.inputE1.tagId,"tag1");
        InstitutionEntity institutionE1 = new InstitutionEntity("insitution1","Bank1");
        ItemEntity itemE1 = new ItemEntity(this.inputE1.itemId,"insitution1","access_token1");
        AccountEntity accountE1 = new AccountEntity(this.inputE1.accountId,this.inputE1.itemId,"acount_name1");
        Date date1 = new Date();
        TransactionEntity transactionE1 = new TransactionEntity(this.inputE1.transactionId,this.inputE1.accountId,this.inputE1.itemId,"transaction1",2.00,false,date0);

        //inserting items in database
        tagDao.insert(tagE0);
        tagDao.insert(tagE1);
        institutionDao.insert(institutionE0);
        institutionDao.insert(institutionE1);
        itemDao.insert(itemE0);
        itemDao.insert(itemE1);
        accountDoa.insert(accountE0);
        accountDoa.insert(accountE1);
        transactionDao.insert(transactionE0);
        transactionDao.insert(transactionE1);

        //inserting testing items
        this.cataloggedDao.insert(this.inputE0);
        this.cataloggedDao.insert(this.inputE1);

    }

    @Test
    public void test_insert() {}

    @Test
    public void test_delete()
    {
        this.cataloggedDao.delete(this.inputE1);
        List<CataloggedEntity> outputEs = this.cataloggedDao.getAll();
        assertThat(outputEs.size(),equalTo(1));
    }


//    @Test
//    public void isAlreadyCatalogged()
//    {
//        Flowable<Boolean> outputR = this.cataloggedDao.isAlreadyCatalogged(this.inputE0.itemId,this.inputE0.accountId);
//        assertThat(outputR,equalTo(false));
//    }



}
