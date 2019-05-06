package com.savantspender.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.savantspender.db.entity.Tag;
import com.savantspender.db.entity.Transaction;

import java.util.List;

public class CategorizerViewModel extends ViewModel {
    private final MutableLiveData<List<? extends Transaction>> mToCategorize = new MutableLiveData<>();

    public void setTransactions(List<? extends Transaction> transactions) {
        mToCategorize.postValue(transactions);
    }

    public LiveData<List<? extends Transaction>> needTags() {
        return mToCategorize;
    }

    public void categorize(List<? extends Tag> usingTags) {
        Log.e("Spender", "would categorize using " + usingTags.size() + " tags");
    }
}
