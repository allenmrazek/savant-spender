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

    public void enterCategorizeMode(List<Transaction> transactions) {
        Log.e("Spender", "enterCategorizeMode");
        mBeginCategorize.postValue(new Event<>(transactions));
    }

    public LiveData<Event<List<Transaction>>> beginCategorize() {
        return mBeginCategorize;
    }
}
