package com.savantspender.model;


import com.savantspender.db.AppDatabase;

import java.util.concurrent.Executor;

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
}
