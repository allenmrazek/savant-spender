package com.savantspender;

import android.app.Application;
import android.util.Log;

import com.savantspender.db.AppDatabase;
import com.savantspender.model.DataRepository;

public class SavantSpender extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();

        if (BuildConfig.DEBUG) {
            Log.w("Spender", "initializing SavantSpender");
        }
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
