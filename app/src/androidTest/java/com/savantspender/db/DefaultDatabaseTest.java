package com.savantspender.db;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;


import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.GoalTagsEntity;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.TransactionEntity;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;

import java.io.IOException;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class DefaultDatabaseTest {
    protected AppDatabase mDatabase;
    //valid naming numonics just update here to change unless otherwise noted
    protected static final String tagName = "tagName";
    protected static final String institutionId = "institution";
    protected static final String institutionName = "bank";
    protected static final String itemId = "item";
    protected static final String token = "token";
    protected static final String accountId = "account";
    protected static final String accountName = "accountName";
    protected static final String goalId = "goal";
    protected static final double dollarValue = 1.00; //updating here will error
    protected static final String transactionId = "transaction";
    protected static final String transactionName = "transactionName";
    protected static boolean pending; //updating here will error
    //Tagid not included as it is just the input value

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void createDb()
    {
        Context context = ApplicationProvider.getApplicationContext();

        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }


    @After
    public void closeDb() throws IOException {
        mDatabase.close();
    }

    public void fullTransactionVerifier(TransactionEntity outputE, TransactionEntity expectedE)
    {
        assertThat(outputE,is(instanceOf(TransactionEntity.class)));
        assertThat(outputE.id,is(equalTo(expectedE.id)));
        assertThat(outputE.pending,is(equalTo(expectedE.pending)));
        assertThat(outputE.name,is(equalTo(expectedE.name)));
        assertThat(outputE.amount,is(equalTo(expectedE.amount)));
        assertThat(outputE.accountId,is(equalTo(expectedE.accountId)));
        assertThat(outputE.itemId,is(equalTo(expectedE.itemId)));
    }
    public void fullTagVerifier(TagEntity outputE, TagEntity expectedE)
    {
        assertThat(outputE,is(instanceOf(TagEntity.class)));
        assertThat(outputE.getId(),is(equalTo(expectedE.getId())));
        assertThat(outputE.getName(),is(equalTo(expectedE.getName())));
    }

    public void fullInstitutionVerifier(InstitutionEntity outputE, InstitutionEntity expectedE)
    {
        assertThat(outputE,is(instanceOf(InstitutionEntity.class)));
        assertThat(outputE.id,is(equalTo(expectedE.id)));
        assertThat(outputE.name,is(equalTo(expectedE.name)));
    }

    public void fullItemVerifier(ItemEntity outputE, ItemEntity expectedE)
    {
        assertThat(outputE,is(instanceOf(ItemEntity.class)));
        assertThat(outputE.id,is(equalTo(expectedE.id)));
        assertThat(outputE.institutionId,is(equalTo(expectedE.institutionId)));
        assertThat(outputE.access_token,is(equalTo(expectedE.access_token)));

    }
    public void fullAccountVerifier(AccountEntity outputE, AccountEntity expectedE)
    {
        assertThat(outputE,is(instanceOf(AccountEntity.class)));
        assertThat(outputE.id,is(equalTo(expectedE.id)));
        assertThat(outputE.name,is(equalTo(expectedE.name)));
        assertThat(outputE.itemId,is(equalTo(expectedE.itemId)));

    }
    public void fullGoalVerifier(GoalEntity outputE, GoalEntity expectedE)
    {
        assertThat(outputE,is(instanceOf(GoalEntity.class)));
        assertThat(outputE.name,is(equalTo(expectedE.name)));
        assertThat(outputE.amount,is(equalTo(expectedE.amount)));

    }
    public void fullGoalTagTrackerVerifier(GoalTagsEntity outputE, GoalTagsEntity expectedE)
    {
        assertThat(outputE,is(instanceOf(GoalTagsEntity.class)));
        assertThat(outputE.goalId,is(equalTo(expectedE.goalId)));
        assertThat(outputE.tagId,is(equalTo(expectedE.tagId)));

    }
    public void fullCataloggedVerifier(CataloggedEntity outputE, CataloggedEntity expectedE)
    {
        assertThat(outputE,is(instanceOf(CataloggedEntity.class)));
        assertThat(outputE.accountId,is(equalTo(expectedE.accountId)));
        assertThat(outputE.itemId,is(equalTo(expectedE.itemId)));
        assertThat(outputE.transactionId,is(equalTo(expectedE.transactionId)));
        assertThat(outputE.tagId,is(equalTo(expectedE.tagId)));

    }


    public void fullpopulate(int amount) throws InterruptedException
    {
        prepopulateDB("Tag",amount);
        prepopulateDB("Institution",amount);
        prepopulateDB("Item",amount);
        prepopulateDB("Account",amount);
        prepopulateDB("Transaction",amount);
        prepopulateDB("Goal",amount);
        prepopulateDB("Catalogged",amount);
        prepopulateDB("GoalTagTracker",amount);

    }
    public TagEntity tagGenorator(int i)
    {
        int tagId = i;
        String tagName = this.tagName + Integer.toString(i);
        return new TagEntity(tagId,tagName);
    }

    public InstitutionEntity institutionGenorator(int i)
    {
        String institutionId = this.institutionId + Integer.toString(i);
        String bankName = this.institutionName + Integer.toString(i);
        return new InstitutionEntity(institutionId,bankName);
    }

    public ItemEntity itemGenorator(int i)
    {
        String itemId = this.itemId + Integer.toString(i);
        String institutionId = this.institutionId + Integer.toString(i);
        String token = this.token + Integer.toString(i);
        return new ItemEntity(itemId,institutionId,token);
    }

    public AccountEntity accountGenorator(int i)
    {
        String accountId =  this.accountId + Integer.toString(i);
        String itemId = this.itemId + Integer.toString(i);
        String accountName = this.accountName + Integer.toString(i);
        return new AccountEntity(accountId,itemId,accountName);
    }

//    public GoalEntity goalGenorator(int i)
//    {
//        String goalId = this.goalId + Integer.toString(i);
//        double dollar_amount = this.dollarValue + (double)i;
//        return new GoalEntity(goalId,dollar_amount);
//    }

    public CataloggedEntity cataloggedGenorator(int i)
    {
        String accountId =  this.accountId + Integer.toString(i);
        String itemId = this.itemId + Integer.toString(i);
        String transactionId = this.transactionId + Integer.toString(i);
        int tagId = i;
        return new CataloggedEntity(accountId,transactionId,itemId,tagId);
    }

//    public GoalTagsEntity goalTagTrackerGenorator(int i)
//    {
//        String goalId = this.goalId + Integer.toString(i);
//        int tagId = i;
//        return new GoalTagsEntity(goalId,tagId);
//    }

    public TransactionEntity transactionGenorator(int i)
    {
        String transactionId = this.transactionId + Integer.toString(i);
        String accountId = this.accountId + Integer.toString(i);
        String itemId = this.itemId + Integer.toString(i);
        String transactionName = this.transactionName + Integer.toString(i);
        double dollarValue = this.dollarValue + (double)i;
        Date date = Calendar.getInstance().getTime();
        if (i % 2 == 0)  {this.pending = true;}
        else {this.pending = false;}
        return new TransactionEntity(transactionId,accountId,itemId,transactionName,dollarValue,this.pending,date);
    }

    public void prepopulateDB(String Entity, int amount)
    {
        for (int i = 0; i <= amount; i++) {
            switch (Entity) {
                 case "Tag":
                     mDatabase.tagDao().insert(this.tagGenorator(i));
                     break;
                 case "Institution":
                     mDatabase.institutionDao().insert(this.institutionGenorator(i));
                     break;
                case "Item":
                     mDatabase.itemDao().insert(this.itemGenorator(i));
                       break;
                 case "Account":
                     mDatabase.accountDao().insert(this.accountGenorator(i));
                      break;
//                  case "Goal":
//                      mDatabase.goalDoa().insert(this.goalGenorator(i));
//                       break;
                 case "Transaction":

                     mDatabase.transactionDao().insert(this.transactionGenorator(i));
                        break;
                  case "Catalogged":
                      mDatabase.cataloggedDao().insert(this.cataloggedGenorator(i));
                     break;
//                 case "GoalTagTracker":
//                     mDatabase.goalTagTrackerDoa().insert(this.goalTagTrackerGenorator(i));
//                      break;
            }
        }
    }
}
