package com.savantspender.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.savantspender.Event;
import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.model.DataRepository;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkViewModel extends ViewModel {
    // this is merely a helper object to convert JSON into a more easily used POJO
    private static class PlaidLink_ItemData {
        private String PublicToken;
        private String InstitutionId;
        private String InstitutionName;
        private List<PlaidLink_Account> Accounts;

        public PlaidLink_ItemData(@NonNull String publicToken, @NonNull String instId, @NonNull String instName, @NonNull Collection<PlaidLink_Account> accounts) {
            PublicToken = publicToken;
            InstitutionId = instId;
            InstitutionName = instName;
            Accounts = new LinkedList<>(accounts);
        }

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


    private MutableLiveData<Event<Pair<Integer, Intent>>> mIntent = new MutableLiveData<>();
    private MutableLiveData<Event<Void>> mAuthorizationEvent = new MutableLiveData<>();

    private final DataRepository mRepository;
    private final Executor mNetworkIo;
    private final Executor mDiskIo;


    public LinkViewModel(@NonNull DataRepository repository, @NonNull Executor networkIo, @NonNull Executor diskIo) {
        mRepository = repository;
        mNetworkIo = networkIo;
        mDiskIo = diskIo;
    }

    // If the fragment is finished (whether successful or unsuccessful)
    public LiveData<Event<Pair<Integer, Intent>>> completion() {
        return mIntent;
    }

    // Authorization step begins
    public LiveData<Event<Void>> authorization() { return mAuthorizationEvent; }



    public void setCancelled() {
        mIntent.postValue(new Event<>(new Pair<>(Activity.RESULT_CANCELED, new Intent())));
    }


    public void setError(int errorCode, String message) {
        Intent errorDetails = new Intent();

        errorDetails.putExtra("errorCode", errorCode);
        errorDetails.putExtra("errorMessage", message);

        mIntent.postValue(new Event<>(new Pair<>(Activity.RESULT_CANCELED, errorDetails)));
    }


    // only ViewModel will know if everything worked
    private void setSuccess(Intent details) {
        mIntent.postValue(new Event<>(new Pair<>(Activity.RESULT_OK, details)));
    }


    // potential item created, shift into authorization mode and exchange tokens
    public void extractLinkDetails(final @NonNull HashMap<String, String> linkData) {
        // todo: handle case where user has selected the same account twice (invalidate access token and re-exchange?)

        Log.i("Spender", "extracting link details");

        // immediately shift the view into auth mode so the user can tell we're busy
        mAuthorizationEvent.postValue(new Event<>(null));

        mDiskIo.execute(() -> {
            PlaidLink_ItemData pojo = convertJsonToPojo(linkData);

            // since instid is a FK of items and we already know it should exist
            mRepository.ensureInstitutionExists(pojo.InstitutionId, pojo.InstitutionName);

            // kickoff token exchange
            mNetworkIo.execute(() -> exchangeToken(pojo));
        });


    }

    // this is the bit that should normally be protected behind a remote server
    @WorkerThread
    private void exchangeToken(PlaidLink_ItemData data) {
        Log.i("Spender", "Exchanging token " + data.PublicToken);

        final PlaidClient client = PlaidClient.newBuilder()
                .clientIdAndSecret("5c54506a47679a00117ebada", "7be0aefd21b235efcb5717101969ca")
                .publicKey("efebb105ab905b6e6cbe0a12e4689b") // optional. only needed to call endpoints that require a public key
                .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
                .build();

        client
                .service()
                .itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(data.PublicToken))
                .enqueue(new Callback<ItemPublicTokenExchangeResponse>() {
                    @Override
                    public void onResponse(Call<ItemPublicTokenExchangeResponse> call, Response<ItemPublicTokenExchangeResponse> response) {
                        Log.d("Spender", "received token exchange response");
                        mNetworkIo.execute(() -> onTokenExchangeResponse(response, data));
                    }

                    @Override
                    public void onFailure(Call<ItemPublicTokenExchangeResponse> call, Throwable t) {
                        Log.e("Spender", "token exchange call failed");
                        setError(1001 /* todo: better ec? */, t.getMessage());
                        return;
                    }
                });
    }

    @WorkerThread
    private void onTokenExchangeResponse(final Response<ItemPublicTokenExchangeResponse> response, final PlaidLink_ItemData data) {
        if (!response.isSuccessful()) {
            Log.e("Spender", "token exchange response was not successful: " + response.code() + ", " + response.message());
            setError(response.code(), response.message());
            return;
        }

        try {
            mRepository.insertNewItem(response.body().getItemId(), data.InstitutionId, response.body().getAccessToken());
        } catch (Exception e) { // TODO: better error handling
            setError(1002 /* todo: better ec */, e.getMessage());
            return;
        }

        Intent details = new Intent();

        details.putExtra("itemId", response.body().getItemId());
        details.putExtra("access_token", response.body().getAccessToken());
        details.putExtra("institutionId", data.InstitutionId);
        details.putExtra("institutionName", data.InstitutionName);

        Log.i("Spender", "successfully exchanged token");

        setSuccess(details);
    }


    private PlaidLink_ItemData convertJsonToPojo(HashMap<String, String> linkData) throws RuntimeException
    {
        String instName = linkData.get("institution_name");
        String instId = linkData.get("institution_id");

        if (instName == null || instId == null)
            throw new RuntimeException("invalid institution id or name");

        String publicToken = linkData.get("public_token");

        if (publicToken == null)
            throw new RuntimeException("invalid public token");

        // retrieve accounts
        String json = linkData.get("accounts");
        Gson gson = new Gson();

        Type collectionType = new TypeToken<Collection<PlaidLink_ItemData.PlaidLink_Account>>(){}.getType();
        Collection<PlaidLink_ItemData.PlaidLink_Account> accounts = gson.fromJson(json, collectionType);

        return new PlaidLink_ItemData(publicToken, instId, instName, accounts);
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
            return (T) new LinkViewModel(mApplication.getRepository(), mApplication.getExecutors().networkIO(), mApplication.getExecutors().diskIO());
        }
    }
}
