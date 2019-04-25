package com.savantspender;

import android.app.Application;
import android.util.Log;

import com.savantspender.AppExecutors;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.EmployeeEntity;
import com.savantspender.model.DataRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SavantSpender extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
