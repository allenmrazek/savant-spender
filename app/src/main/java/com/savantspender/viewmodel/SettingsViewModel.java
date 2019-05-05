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
