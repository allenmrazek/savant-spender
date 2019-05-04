package com.savantspender.model;


import android.database.sqlite.SQLiteConstraintException;

import androidx.annotation.NonNull;

import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.InstitutionEntity;
import com.savantspender.db.entity.ItemEntity;

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

    public void ensureInstitutionExists(@NonNull String id, @NonNull String name) {
        InstitutionEntity ie = new InstitutionEntity(id, name);

        mDatabase.institutionDao().insert(ie);
    }

    public void insertNewItem(@NonNull String itemId, @NonNull String instId, @NonNull String accessToken) {
        // todo: check if there is already an item for this institution, could make problems
        mDatabase.itemDao().insert(new ItemEntity(itemId, instId, accessToken));
    }

    // todo: data we need to expose goes here (stuff that views might be interested in observing)
}
