package com.savantspender.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.savantspender.AppExecutors;
import com.savantspender.db.converter.DateConverter;
import com.savantspender.db.dao.AccountDao;
import com.savantspender.db.dao.CataloggedDao;
import com.savantspender.db.dao.GoalDao;
import com.savantspender.db.dao.GoalTagDao;
import com.savantspender.db.dao.InstitutionDao;
import com.savantspender.db.dao.ItemDao;
import com.savantspender.db.dao.TagDao;
import com.savantspender.db.dao.TransactionDao;
import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.CataloggedEntity;


import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.GoalTagsEntity;

import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.TransactionEntity;
import com.savantspender.util.Constants;


@Database(entities = {
        ItemEntity.class,
        AccountEntity.class,
        InstitutionEntity.class,
        TagEntity.class,
        TransactionEntity.class,
        CataloggedEntity.class,
        GoalEntity.class,
        GoalTagsEntity.class
}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
@SuppressWarnings("deprecation")
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mAppDatabase;
    private static final String DATABASE_NAME = "SavantSpenderDB";


    public abstract ItemDao itemDao();
    public abstract AccountDao accountDao();
    public abstract InstitutionDao institutionDao();
    public abstract TagDao tagDao();
    public abstract TransactionDao transactionDao();
    public abstract CataloggedDao cataloggedDao();
    public abstract GoalDao goalDao();
    public abstract GoalTagDao goalTagDao();

    public static AppDatabase getInstance(final Context appContext, final AppExecutors executors) {
        if (mAppDatabase == null) {
            synchronized (AppDatabase.class) {
                mAppDatabase = buildDatabase(appContext.getApplicationContext(), executors);

                executors.diskIO().execute(() -> {
                    mAppDatabase.insertManualTransactionDummyEntries();
                    mAppDatabase.insertDefaultTags();
                });
            }
        }

        return mAppDatabase;
    }

    public void resetDatabase() {
        clearAllTables();
        insertManualTransactionDummyEntries();
        insertDefaultTags();
    }

    private void insertManualTransactionDummyEntries() {

        if (!mAppDatabase.institutionDao().existsSync(Constants.ManualInstitutionId))
            mAppDatabase.institutionDao().insert(
                    new InstitutionEntity(Constants.ManualInstitutionId, Constants.ManualInstitutionName));

        if (!mAppDatabase.itemDao().exists(Constants.ManualItemId))
            mAppDatabase.itemDao().insert(
                    new ItemEntity(Constants.ManualItemId, Constants.ManualInstitutionId, "na"));

        if (!mAppDatabase.accountDao().existsSync(Constants.ManualAccountId, Constants.ManualItemId))
            mAppDatabase.accountDao().insert(
                    new AccountEntity(
                            Constants.ManualAccountId, Constants.ManualItemId, Constants.ManualAccountName));
    }

    public void insertDefaultTags() {
        TagDao tagDao = mAppDatabase.tagDao();

        String[] defaultTags = new String[] {
                "Food", "Fuel", "Fun", "Fast Food"
        };

        int counter = 0;

        for (String tag : defaultTags) {
            tagDao.upsert(new TagEntity(counter++, tag));
        }
    }


    private static AppDatabase buildDatabase(final Context appContext, final AppExecutors executors) {

        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {

                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Log.w("Spender", "Creating DB for first time");
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                        Log.w("Spender", "Opening existing DB");
                    }
                })
                .build();
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }
}
