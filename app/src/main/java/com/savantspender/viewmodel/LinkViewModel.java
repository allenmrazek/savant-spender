package com.savantspender.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LinkViewModel extends ViewModel {
    private static class PlaidLink_ItemData {
        private String mPublicToken;
        private String mInstitutionId;
        private List<PlaidLink_Account> mAccounts;

        public PlaidLink_ItemData(@NonNull String publicToken, @NonNull String instId, @NonNull Collection<PlaidLink_Account> accounts) {
            mPublicToken = publicToken;
            mInstitutionId = instId;
            mAccounts = new LinkedList<>(accounts);
        }

        public String getPublicToken() { return mPublicToken; }
        public String getInstitutionId() { return mInstitutionId; }
        @NonNull public List<PlaidLink_Account> getAccounts() { return new LinkedList<>(mAccounts); /* send a copy */ }

        public static class PlaidLink_Account {
            private String _id;
            private Meta meta;

            public String getId() { return _id; }
            public String getName() { return meta.name; }

            static class Meta {
                private String name;
            }
        }
    }


    //private MutableLiveData<PlaidLink_ItemData> mItemData = new MutableLiveData<>();
    private MutableLiveData<Pair<Integer, Intent>> mIntent = new MutableLiveData<>();


//    public LiveData<PlaidLink_ItemData> getItemData() {
//        return mItemData;
//    }


    public LiveData<Pair<Integer, Intent>> getCompletionIntent() {
        return mIntent;
    }


    public void setCancelled() {
        mIntent.postValue(new Pair<Integer, Intent>(Activity.RESULT_CANCELED, new Intent()));
    }


    public void setError(int errorCode, String message) {
        Intent errorDetails = new Intent();

        errorDetails.putExtra("errorCode", errorCode);
        errorDetails.putExtra("errorMessage", message);

        mIntent.postValue(new Pair<Integer, Intent>(Activity.RESULT_CANCELED, errorDetails));
    }


    // only ViewModel will know if everything worked
    private void setSuccess() {
        mIntent.postValue(new Pair<Integer, Intent>(Activity.RESULT_OK, new Intent()));
    }


    public void extractLinkDetails(@NonNull HashMap<String, String> linkData) {
        String json = linkData.get("accounts");
        Gson gson = new Gson();

        Type collectionType = new TypeToken<Collection<PlaidLink_ItemData.PlaidLink_Account>>(){}.getType();
        Collection<PlaidLink_ItemData.PlaidLink_Account> tokens = gson.fromJson(json, collectionType);

        for (PlaidLink_ItemData.PlaidLink_Account acc : tokens)
            Log.e("Spender", "Found: " + acc.getName() + " , " + acc.getId());

        //mItemData.postValue(new PlaidLink_ItemData(linkData.get("public_token"), linkData.get("institution_id"), tokens));
    }


    public void exchangedToken(String itemId, String accessToken) {
        // todo: if account metadata missing, download it (could be gibberish on first link)
        // todo: verify that the account isn't already in the database; if it is, its access
        //       token needs to be invalidated and the item possibly deleted if it has no user
        //       accounts associated with it


        // todo: was it successful?

        // temp: assume it was
        Intent intent = new Intent();

        intent.putExtra("itemId", itemId);
        intent.putExtra("accessToken", accessToken);
//        intent.putExtra("public_token", mItemData.getValue().getPublicToken());
//        intent.putExtra("instId", mItemData.getValue().getInstitutionId());

        mIntent.postValue(new Pair<Integer, Intent>(Activity.RESULT_OK, intent));
    }
}
