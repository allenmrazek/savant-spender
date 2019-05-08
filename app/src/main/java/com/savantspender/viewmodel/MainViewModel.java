package com.savantspender.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.savantspender.Event;
import com.savantspender.db.entity.Transaction;

import java.util.List;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Event<List<Transaction>>> mBeginCategorize = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mToast = new MutableLiveData<>();
    private final MutableLiveData<Event<Void>> mCloseNewTransDialog = new MutableLiveData<>();


    public void enterCategorizeMode(List<Transaction> transactions) {
        Log.e("Spender", "enterCategorizeMode");
        mBeginCategorize.postValue(new Event<>(transactions));
    }

    public LiveData<Event<List<Transaction>>> beginCategorize() {
        return mBeginCategorize;
    }
    public LiveData<Event<String>> toastMessage() {
        return mToast;
    }
    public LiveData<Event<Void>> closingNewTransDlg() { return mCloseNewTransDialog; }

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

        // todo: begin process
    }

}
