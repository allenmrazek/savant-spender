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
import com.savantspender.R;
import com.savantspender.db.converter.DateConverter;
import com.savantspender.db.dao.AccountDao;
import com.savantspender.db.dao.EmployeeDao;
import com.savantspender.db.dao.InstitutionDao;
import com.savantspender.db.dao.ItemDao;
import com.savantspender.db.dao.ProjectDao;
import com.savantspender.db.dao.TagDao;
import com.savantspender.db.dao.TransactionDao;
import com.savantspender.db.dao.WorksOnDao;
import com.savantspender.db.entity.AccountEntity;
import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.EmployeeEntity;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;
import com.savantspender.db.entity.ProjectEntity;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.TransactionEntity;
import com.savantspender.db.entity.WorksOnEntity;

import java.util.concurrent.Executors;


@Database(entities = {
        EmployeeEntity.class,
        WorksOnEntity.class,
        ProjectEntity.class,

        ItemEntity.class,
        AccountEntity.class,
        InstitutionEntity.class,
        TagEntity.class,
        TransactionEntity.class,
        CataloggedEntity.class
}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
@SuppressWarnings("deprecation")
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mAppDatabase;
    private static final String DATABASE_NAME = "SavantSpenderDB";


    public abstract EmployeeDao employeeDao();
    public abstract WorksOnDao worksOnDao();
    public abstract ProjectDao projectDao();

    public abstract ItemDao itemDao();
    public abstract AccountDao accountDao();
    public abstract InstitutionDao institutionDao();
    public abstract TagDao tagDao();
    public abstract TransactionDao transactionDao();

    public static AppDatabase getInstance(final Context appContext, final AppExecutors executors) {
        if (mAppDatabase == null) {
            synchronized (AppDatabase.class) {
                mAppDatabase = buildDatabase(appContext.getApplicationContext(), executors);
            }
        }

        return mAppDatabase;
    }

    private interface IFunc {
        String get(int id);
    }

    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {

        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            IFunc res = id -> appContext.getApplicationContext().getResources().getString(id);

                            // manual entries won't be associated with any item or account,
                            // but the db requires that all transactions contain valid references
                            // to them

                            mAppDatabase.institutionDao().insert(
                                    new InstitutionEntity(
                                            res.get(R.string.db_manual_itemId),
                                            res.get(R.string.db_manual_instName)));

                            mAppDatabase.itemDao().insert(
                                    new ItemEntity(
                                            res.get(R.string.db_manual_itemId),
                                            res.get(R.string.db_manual_instId),
                                            res.get(R.string.db_manual_accessToken)));

                            mAppDatabase.accountDao().insert(
                                    new AccountEntity(
                                            res.get(R.string.db_manual_accountId),
                                            res.get(R.string.db_manual_itemId),
                                            res.get(R.string.db_manual_accountName)
                                    ));
                        });
                    }
                })
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
