package com.savantspender.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.model.DataRepository;

import java.util.concurrent.Executor;

public class SettingsViewModel extends ViewModel {
    private AppDatabase mDatabase;
    private Executor mExecutor;

    private SettingsViewModel(final AppDatabase mDatabase, final Executor executor) {
        this.mDatabase = mDatabase;
        this.mExecutor = executor;
    }

    public void onDeleteDatabaseClicked() {
        Log.w("Spender", "Deleting database");
        mExecutor.execute(() -> mDatabase.clearAllTables());
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final SavantSpender mApplication;

        public Factory(@NonNull Application application) {
            mApplication = (SavantSpender)application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new SettingsViewModel(mApplication.getDatabase(), mApplication.getExecutors().diskIO());
        }
    }
}
