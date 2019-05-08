package com.savantspender.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.savantspender.Event;
import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.Transaction;
import com.savantspender.db.entity.TransactionEntity;
import com.savantspender.model.DataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class TransactionViewModel extends ViewModel {
    private final LiveData<List<? extends Transaction>> mUnsortedTrans;
    private final LiveData<List<? extends Transaction>> mSortedTrans;

    private final DataRepository mRepository;
    private final AppDatabase mDatabase;
    private final Executor mDiskIO;


    public TransactionViewModel(
            @NonNull final DataRepository repository,
            final AppDatabase db,
            final Executor diskIO) {

        mRepository = repository;
        mDatabase = db;
        mDiskIO = diskIO;

        // todo: actual filtering, simple passthrough for now
        mSortedTrans = Transformations.map(repository.sortedTransactions(), l -> l);
        mUnsortedTrans = Transformations.map(repository.unsortedTransactions(), l -> l);
    }

    public LiveData<List<? extends Transaction>> unsortedTransactions() {
        return mUnsortedTrans;
    }
    public LiveData<List<? extends Transaction>> sortedTransactions() {
        return mSortedTrans;
    }

    protected List<TransactionEntity> getSelected(List<Transaction> transactions) {
        List<TransactionEntity> selected = new ArrayList<>(transactions.size());

        for (Transaction t : transactions)
            if (t.isSelected())
                selected.add((TransactionEntity)t);

        return selected;
    }


    public void doUnsortTransactions(List<Transaction> transactions) {
        mDiskIO.execute(() -> {
            mDatabase.cataloggedDao().untag(getSelected(transactions));
        });
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final SavantSpender mApplication;

        public Factory(@NonNull Application application) {
            mApplication = (SavantSpender)application;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TransactionViewModel(mApplication.getRepository(), mApplication.getDatabase(), mApplication.getExecutors().diskIO());
        }
    }

}
