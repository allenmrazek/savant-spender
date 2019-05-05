package com.savantspender.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.Transformations;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.savantspender.AppExecutors;
import com.savantspender.R;
import com.savantspender.SavantSpender;
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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


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

                executors.diskIO().execute(() -> {
                    mAppDatabase.insertManualTransactionDummyEntries();
                });
            }
        }

        return mAppDatabase;
    }

    private interface IFunc {
        String get(int id);
    }

    public void resetDatabase() {
        clearAllTables();
        insertManualTransactionDummyEntries();
    }

    private void insertManualTransactionDummyEntries() {
        // used for manual transactions

        mAppDatabase.institutionDao().insert(
                new InstitutionEntity("manual_inst_id", "Manual Entry"));

        mAppDatabase.itemDao().insert(
                new ItemEntity("manual_item_id", "manual_inst_id", "na"));

        mAppDatabase.accountDao().insert(
                new AccountEntity(
                        "manual_account", "manual_item_id", "Manual Entry"));
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
