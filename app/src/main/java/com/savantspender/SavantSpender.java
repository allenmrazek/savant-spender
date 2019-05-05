package com.savantspender;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.savantspender.db.AppDatabase;
import com.savantspender.model.DataRepository;
import com.savantspender.worker.TestWorker;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class SavantSpender extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
        initializePeriodicWorkRequests();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }

    public AppExecutors getExecutors() {
        return mAppExecutors;
    }


    private void initializePeriodicWorkRequests() {
        String periodicWorkTag = getResources().getString(R.string.work_dl_trans_p);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // todo: check setting option, limit to wifi if required

        PeriodicWorkRequest downloadTransactions =
                new PeriodicWorkRequest.Builder(TestWorker.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .addTag(periodicWorkTag)
                        .build();

        // ensure download transaction task is scheduled; this will occur in background even if
        // app not in foreground or active
        WorkManager.getInstance().enqueueUniquePeriodicWork(periodicWorkTag, ExistingPeriodicWorkPolicy.KEEP, downloadTransactions);
    }
}
