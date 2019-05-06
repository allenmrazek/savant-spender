package com.savantspender.db;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.savantspender.db.dao.InstitutionDao;
import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.GoalTagTrackerEntity;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.TransactionEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class DefaultDatabaseTest {
    protected AppDatabase mDatabase;
    //valid naming numonics just update here to change unless otherwise noted
    protected String tagName = "tagName";
    protected String institutionId = "institution";
    protected String institutionName = "bank";
    String itemId = "item";
    String token = "token";
    String accountId = "account";
    String accountName = "accountName";
    String goalId = "goal";
    double dollarValue = 1.00; //updating here will error
    String transactionId = "transaction";
    String transactionName = "transactionName";
    boolean pending; //updating here will error
    //Tagid not included as it is just the input value

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();

        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
    }


    @After
    public void closeDb() throws IOException {
        mDatabase.close();
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
        tagName = this.tagName + Integer.toString(i);
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

    public GoalEntity goalGenorator(int i)
    {
        String goalId = this.goalId + Integer.toString(i);
        double dollar_amount = this.dollarValue + (double)i;
        return new GoalEntity(goalId,dollar_amount);
    }

    public CataloggedEntity cataloggedGenorator(int i)
    {
        String accountId =  this.accountId + Integer.toString(i);
        String itemId = this.itemId + Integer.toString(i);
        String transactionId = this.transactionId + Integer.toString(i);
        int tagId = i;
        return new CataloggedEntity(accountId,transactionId,itemId,tagId);
    }

    public GoalTagTrackerEntity goalTagTrackerGenorator(int i)
    {
        String goalId = this.goalId + Integer.toString(i);
        int tagId = i;
        return new GoalTagTrackerEntity(goalId,tagId);
    }

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

    public void prepopulateDB(String Entity, int amount) throws InterruptedException
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
                  case "Goal":
                      mDatabase.goalDoa().insert(this.goalGenorator(i));
                       break;
                 case "Transaction":

                     mDatabase.transactionDao().insert(this.transactionGenorator(i));
                        break;
                  case "Catalogged":
                      mDatabase.cataloggedDao().insert(this.cataloggedGenorator(i));
                     break;
                 case "GoalTagTracker":
                     mDatabase.goalTagTrackerDoa().insert(this.goalTagTrackerGenorator(i));
                      break;
            }
        }
    }
}
