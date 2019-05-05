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
    private final LiveData<List<? extends Transaction>> mUncataloggedTrans;
    private final DataRepository mRepository;

    public TransactionViewModel(@NonNull DataRepository repository) {
        mRepository = repository;

        mUncataloggedTrans = Transformations.map(repository.spendingTransactions(), l -> {
            return l;
        });
    }

    public LiveData<List<? extends Transaction>> uncataloggedTransactions() {
        return mUncataloggedTrans;
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
