package com.savantspender.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.savantspender.SavantSpender;
import com.savantspender.db.entity.Transaction;
import com.savantspender.model.DataRepository;

import java.util.List;

public class TransactionViewModel extends ViewModel {
    private final LiveData<List<? extends Transaction>> mUnsortedTrans;
    private final LiveData<List<? extends Transaction>> mSortedTrans;

    private final DataRepository mRepository;

    public TransactionViewModel(@NonNull DataRepository repository) {
        mRepository = repository;

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
            return (T) new TransactionViewModel(mApplication.getRepository());
        }
    }
}
