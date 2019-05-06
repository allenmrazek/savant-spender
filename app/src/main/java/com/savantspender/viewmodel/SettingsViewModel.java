package com.savantspender.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.savantspender.Event;
import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.CataloggedEntity;
import com.savantspender.db.entity.TagEntity;

import java.util.concurrent.Executor;

public class SettingsViewModel extends ViewModel {
    private AppDatabase mDatabase;
    private Executor mExecutor;
    private MutableLiveData<Event<String>> mToastMessage = new MutableLiveData<>();

    private SettingsViewModel(final AppDatabase mDatabase, final Executor executor) {
        this.mDatabase = mDatabase;
        this.mExecutor = executor;
    }

    public LiveData<Event<String>> toastMessages() {
        return mToastMessage;
    }

    public void onDeleteDatabaseClicked() {

        mExecutor.execute(() -> {
            Log.w("Spender", "Deleting database");
            mDatabase.resetDatabase();
            mToastMessage.postValue(new Event<>("Databased cleared!"));
        });
    }

    public void onGenerateRandomTransactionClicked() {
        Log.w("Spender", "Generate random transaction here");

        // todo: insert dummy instid if none
        // todo: add toast message
    }


    public void onDeleteTransactionsClicked() {
        Log.w("Spender", "Deleting transactions");

        mExecutor.execute(() -> {
            mDatabase.beginTransaction();

            try {
                for (CataloggedEntity e : mDatabase.cataloggedDao().getAll()) {
                    mDatabase.cataloggedDao().delete(e);
                }

                mDatabase.transactionDao().deleteAll();
            } finally {
                mDatabase.endTransaction();
            }

            mToastMessage.postValue(new Event<>("Transactions cleared!"));
        });
    }


    public void onDeleteGoalsClicked() {
        Log.w("Spender", "Deleting goals");
        mToastMessage.postValue(new Event<>("not implemented"));
    }

    public void onDeleteTagsClicked() {
        mExecutor.execute(() -> {
            Log.i("Spender", "Deleting tags");

            mDatabase.beginTransaction();
            for (TagEntity te : mDatabase.tagDao().getTagsSync())
                mDatabase.tagDao().delete(te);

            mDatabase.insertDefaultTags();
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();

            mToastMessage.postValue(new Event<>("Tags deleted!"));
            Log.i("Spender", "Tags were deleted");
        });
    }

    public void onResetCataloggedClick() {
        mExecutor.execute(() -> {
            Log.i("Spender", "Resetting transaction tags");

            mDatabase.beginTransaction();
            for (CataloggedEntity ce : mDatabase.cataloggedDao().getAll()) {
                mDatabase.cataloggedDao().delete(ce);
            }
            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();

            mToastMessage.postValue(new Event<>("Transaction tags reset!"));
            Log.i("Spender", "Transaction tags were reset");
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
            return (T) new SettingsViewModel(mApplication.getDatabase(), mApplication.getExecutors().diskIO());
        }
    }
}
