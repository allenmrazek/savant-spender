package com.savantspender.model;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.TransactionEntity;

import java.util.List;

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


    public LiveData<List<TransactionEntity>> spendingTransactions() {
        return mDatabase.transactionDao().getSpendingTransactions();
    }

    public LiveData<List<TransactionEntity>> unsortedTransactions() {
        return mDatabase.transactionDao().getUnsortedTransactions();
    }

    public LiveData<List<TransactionEntity>> sortedTransactions() {
        return mDatabase.transactionDao().getSortedTransactions();
    }


    // todo: data we need to expose goes here (stuff that views might be interested in observing)

    // todo: accounts have changed
    // todo: transactions have changed
    // todo: tags have changed
    // todo: catalogged entries have changed
    // todo: goals have changed
}
