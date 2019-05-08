package com.savantspender.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.savantspender.Event;
import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.Tag;
import com.savantspender.db.entity.Transaction;
import com.savantspender.db.entity.TransactionEntity;
import com.savantspender.worker.UpdateGoalsWorker;

import java.util.List;
import java.util.concurrent.Executor;

public class CategorizerViewModel extends ViewModel {
    private final MutableLiveData<List<? extends Transaction>> mToCategorize = new MutableLiveData<>();
    private final AppDatabase mDatabase;
    private final Executor mDiskIO;


    public CategorizerViewModel(@NonNull AppDatabase database, @NonNull Executor diskIO) {
        mDatabase = database;
        mDiskIO = diskIO;
    }

    public void setTransactions(List<? extends Transaction> transactions) {
        mToCategorize.postValue(transactions);
    }

    public LiveData<List<? extends Transaction>> needTags() {
        return mToCategorize;
    }


    public void categorize(List<? extends Tag> usingTags, List<? extends Transaction> transactions) {
        mDiskIO.execute(() -> {

            try {
                mDatabase.beginTransaction();

                for (Transaction transaction : transactions) {
                    TransactionEntity te = (TransactionEntity) transaction;

                    for (Tag tag : usingTags) {
                        mDatabase.cataloggedDao().insert(
                                new CataloggedEntity(
                                        te.accountId,
                                        te.id,
                                        te.itemId,
                                        tag.getId()
                                ));
                        Log.i("Spender", "Categorized " + transaction.getName() + " as " + tag.getName());

                        tag.setSelected(false);
                    }
                }
                mDatabase.setTransactionSuccessful();

                // update transactions
                // todo: constraints?

                OneTimeWorkRequest update = new OneTimeWorkRequest.Builder(UpdateGoalsWorker.class)
                        .setConstraints(new Constraints.Builder().build()).build();

                WorkManager.getInstance().enqueue(update);
            } finally {
                mDatabase.endTransaction();
            }
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
            return (T) new CategorizerViewModel(mApplication.getDatabase(), mApplication.getExecutors().diskIO());
        }
    }
}
