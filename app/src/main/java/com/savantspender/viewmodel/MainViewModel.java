package com.savantspender.viewmodel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
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
import com.savantspender.db.entity.Transaction;
import com.savantspender.db.entity.TransactionEntity;
import com.savantspender.model.DataRepository;
import com.savantspender.util.Constants;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Event<List<Transaction>>> mBeginCategorize = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mToast = new MutableLiveData<>();
    private final MutableLiveData<Event<Void>> mCloseNewTransDialog = new MutableLiveData<>();
    private final MutableLiveData<Event<Void>> mManualTransCreated = new MutableLiveData<>();

    private final AppDatabase mDatabase;
    private final Executor mDiskIO;
    private final DataRepository mRepository;

    public MainViewModel(AppDatabase database, DataRepository repository, Executor diskIO) {
        mDatabase = database;
        mDiskIO = diskIO;
        mRepository = repository;
    }


    public void enterCategorizeMode(List<Transaction> transactions) {
        mBeginCategorize.postValue(new Event<>(transactions));
    }

    public LiveData<Event<List<Transaction>>> beginCategorize() {
        return mBeginCategorize;
    }
    public LiveData<Event<String>> toastMessage() {
        return mToast;
    }
    public LiveData<Event<Void>> closingNewTransDlg() { return mCloseNewTransDialog; }
    public LiveData<Event<Void>> createdTransaction() { return mManualTransCreated; }

    private void makeToast(String text) {
        mToast.postValue(new Event<>(text));
    }


    public void createManualTransaction(String description, String amount) {
        if (description == null || description.length() == 0) {
            makeToast("must provide description");
            return;
        }

        if (amount == null || amount.length() == 0) {
            makeToast("must specify amount");
            return;
        }

        double amt = 0.0;

        try {
            amt = Double.parseDouble(amount);
        } catch (NumberFormatException nfe) {
            makeToast("specify a valid amount");
            return;
        }

        if (amt <= 0.0) {
            makeToast("specify a positive amount");
            return;
        }

        // hacky :( todo: better id
        final String transId = UUID.randomUUID().toString();
        final double transAmount = amt;

        mDiskIO.execute(() -> {
            try {
                mDatabase.transactionDao().insert(new TransactionEntity(transId, Constants.ManualAccountId, Constants.ManualItemId, description, transAmount, false, Calendar.getInstance().getTime()));

                // finished, tell dialog to close
                mCloseNewTransDialog.postValue(new Event<>(null));

                makeToast("Added " + description);
                mManualTransCreated.postValue(new Event<>(null));
            } catch (SQLiteConstraintException sqe) {
                makeToast("failed");
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
            return (T) new MainViewModel(mApplication.getDatabase(), mApplication.getRepository(), mApplication.getExecutors().diskIO());
        }
    }
}
