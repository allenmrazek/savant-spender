package com.savantspender.db;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

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

    @Test
    public void tester()throws InterruptedException
    {
     prepopulateDB("Tag",1);
    }

    public void prepopulateDB(String Entity, int amount) throws InterruptedException
    {
        int tagId;
        String tagName;
        String institutionId;
        String bankName;
        String itemId;
        String token;
        String accountId;
        String accountName;
        String goalId;
        double dollar_amount;
        String transactionId;
        boolean pending;
        Date date;
        String transactionName;
        for (int i = 0; i <= amount; i++) {
            switch (Entity) {
                 case "Tag":
                     tagId = amount;
                     tagName = "tag" + Integer.toString(amount);
                     mDatabase.tagDao().insert(new TagEntity(tagId,tagName));
                     break;
                 case "Institution":
                     institutionId = "institution" + Integer.toString(amount);
                     bankName = "bank" + Integer.toString(amount);
                     mDatabase.institutionDao().insert(new InstitutionEntity(institutionId,bankName));
                     break;
                case "Item":
                    itemId = "item" + Integer.toString(amount);
                    institutionId = "institution" + Integer.toString(amount);
                    token = "token" + Integer.toString(amount);
                     mDatabase.itemDao().insert(new ItemEntity(itemId,institutionId,token));
                       break;
                 case "Account":
                     accountId =  "account" + Integer.toString(amount);
                     itemId = "item" + Integer.toString(amount);
                     accountName = "accountName" + Integer.toString(amount);
                     mDatabase.accountDao().insert(new AccountEntity(accountId,itemId,accountName));
                      break;
                  case "Goal":
                      goalId = "goal" + Integer.toString(amount);
                      dollar_amount = (double)amount + 1.00;
                      mDatabase.goalDoa().insert(new GoalEntity(goalId,dollar_amount));
                       break;
                 case "Transaction":
                     transactionName = "transactionName" + Integer.toString(amount);
                     accountId =  "account" + Integer.toString(amount);
                     itemId = "item" + Integer.toString(amount);
                     dollar_amount = (double)amount + 1.00;
                     transactionId = "transaction" + Integer.toString(amount);
                     date = Calendar.getInstance().getTime();
                     if (amount % 2 == 0)  {pending = true;}
                     else {pending = false;}
                     mDatabase.transactionDao().insert(new TransactionEntity(transactionId,accountId,itemId,transactionName,dollar_amount,pending,date));
                        break;
                  case "Catalogged":
                      accountId =  "account" + Integer.toString(amount);
                      itemId = "item" + Integer.toString(amount);
                      transactionId = "transaction" + Integer.toString(amount);
                      tagId = amount;
                      mDatabase.cataloggedDao().insert(new CataloggedEntity(accountId,transactionId,itemId,tagId));
                     break;
                 case "GoalTagTracker":
                     goalId = "goal" + Integer.toString(amount);
                     tagId = amount;
                     mDatabase.goalTagTrackerDoa().insert(new GoalTagTrackerEntity(goalId,tagId));
                      break;
            }
        }
    }
}
