package com.savantspender.model;


import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.savantspender.BuildConfig;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;
import com.savantspender.service.AuthService;
import com.savantspender.worker.TestWorker;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class DataRepository {
    private static DataRepository mRepository;

    private AppDatabase mDatabase;

    private DataRepository(final AppDatabase db) {
        mDatabase = db;
    }


    public static DataRepository getInstance(final AppDatabase db) {
        if (mRepository == null) {
            synchronized (DataRepository.class) {
                mRepository = new DataRepository(db);
            }
        }

        return mRepository;
    }


    // todo: data we need to expose goes here (stuff that views might be interested in observing)

    // todo: accounts have changed
    // todo: transactions have changed
    // todo: tags have changed
    // todo: catalogged entries have changed
    // todo: goals have changed
}
