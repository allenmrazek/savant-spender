package com.savantspender.db;

import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.GoalTagsEntity;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.TransactionEntity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class Catalogger_UntagTest extends DefaultDatabaseTest {
    private static final String ItemId = "test_itemId";
    private static final String AccountId = "test_accountId";
    private static final String TransactionId = "test_trans_id";
    private static final String InstitutionId = "test_inst_id";
    private static final Date CurrentDate = Calendar.getInstance().getTime();
    private static final int TagId = 123;

    @Override
    @Before
    public void createDb() {
        super.createDb();

        mDatabase.institutionDao().insert(new InstitutionEntity(InstitutionId, "test_inst_name_here"));
        mDatabase.itemDao().insert(new ItemEntity(ItemId, InstitutionId, "access"));
        mDatabase.accountDao().insert(new AccountEntity(AccountId, ItemId, "account_name"));

        mDatabase.transactionDao().insert(new TransactionEntity(TransactionId, AccountId, ItemId, "sometrans1", 200.0, false, CurrentDate));

        mDatabase.tagDao().insert(new TagEntity(TagId, "tag_" + TagId));
        mDatabase.tagDao().insert(new TagEntity(TagId + 1, "tag_" + (TagId + 1)));
    }

    @Test
    public void tag() {
        mDatabase.cataloggedDao().insert(new CataloggedEntity(AccountId, TransactionId, ItemId, TagId));
        mDatabase.cataloggedDao().insert(new CataloggedEntity(AccountId, TransactionId, ItemId, TagId + 1));
    }

    @Test
    public void untag() {
        tag();

        TransactionEntity trans = new TransactionEntity(TransactionId, AccountId, ItemId, "sometrans1", 200.0, false, CurrentDate);

        mDatabase.cataloggedDao().untag(
                Arrays.asList(
                        new TransactionEntity(TransactionId, AccountId, ItemId, "sometrans1", 200.0, false, CurrentDate)));

        assertThat(mDatabase.cataloggedDao().hasTags(trans), equalTo(false));
    }

    @Test
    public void untag_single() {
        tag();

        // add two more dummies that shouldn't be affected
        mDatabase.transactionDao().insert(new TransactionEntity("dummy", AccountId, ItemId, "dummy", 100, false, CurrentDate));
        mDatabase.cataloggedDao().insert(new CataloggedEntity(AccountId, "dummy", ItemId, TagId));
        mDatabase.cataloggedDao().insert(new CataloggedEntity(AccountId, "dummy", ItemId, TagId + 1));


        // this trans (tagged in tag()), to be untagged
        TransactionEntity trans = new TransactionEntity(TransactionId, AccountId, ItemId, "sometrans1", 200.0, false, CurrentDate);

        mDatabase.cataloggedDao().untag(
                Arrays.asList(
                        new TransactionEntity(TransactionId, AccountId, ItemId, "sometrans1", 200.0, false, CurrentDate)));

        assertThat(mDatabase.cataloggedDao().hasTags(trans), equalTo(false));

        List<CataloggedEntity> cataloggedEntities = mDatabase.cataloggedDao().getAll();

        assertThat(cataloggedEntities.size(), equalTo(2));
    }

    @Test
    public void hasTag() {
        tag();
        TransactionEntity trans = new TransactionEntity(TransactionId, AccountId, ItemId, "sometrans1", 200.0, false, CurrentDate);

        assertThat(mDatabase.cataloggedDao().hasTags(trans), equalTo(true));
    }


    @Test
    public void getTransactions() {
//        mDatabase.goalDao().insert(new GoalEntity(0, "test goal", 500, 0));
//        mDatabase.goalTagDao().insert(new GoalTagsEntity(0, TagId));
//
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        mDatabase.cataloggedDao().insert(new CataloggedEntity(AccountId, TransactionId, ItemId, TagId));

        start.add(Calendar.DATE, -1); // yesterday
        end.add(Calendar.DATE, 1); // tomorrow

        assertThat(mDatabase.cataloggedDao().hasTags(new TransactionEntity(TransactionId, AccountId, ItemId, "sometrans1", 0.0, false, CurrentDate)), equalTo(true));

        Date startDate = start.getTime();
        Date endDate = end.getTime();

        assertThat(mDatabase.cataloggedDao().getTransactions(startDate, endDate, Arrays.asList(TagId)).size(), equalTo(1));
    }
}
