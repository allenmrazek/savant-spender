package com.savantspender.ui.frag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemGetRequest;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.TransactionsGetRequest;
import com.plaid.client.request.common.Product;
import com.plaid.client.response.ItemGetResponse;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.ItemStatus;
import com.plaid.client.response.TransactionsGetResponse;
import com.savantspender.R;
import com.savantspender.viewmodel.LinkViewModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthFragment extends Fragment implements /*Observer<LinkViewModel.PlaidLink_ItemData>, */Callback<ItemPublicTokenExchangeResponse> {
    private LinkViewModel mViewModel;
    private PlaidClient mClient;
    private String mAccountID; // todo: remove this

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(LinkViewModel.class);
        //mViewModel.getItemData().observe(this, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        return view;
    }

//    @Override
//    public void onChanged(LinkViewModel.PlaidLink_ItemData item) {
//
//        // this is the bit that should normally be protected behind a remote server
//        mClient = PlaidClient.newBuilder()
//                .clientIdAndSecret("5c54506a47679a00117ebada", "7be0aefd21b235efcb5717101969ca")
//                .publicKey("efebb105ab905b6e6cbe0a12e4689b") // optional. only needed to call endpoints that require a public key
//                .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
//                .build();
//
//        mAccountID = item.getAccounts().get(0).getId();
//
//        mClient.service().itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(item.getPublicToken()))
//                .enqueue(this);
//    }

    @Override
    public void onResponse(Call<ItemPublicTokenExchangeResponse> call, Response<ItemPublicTokenExchangeResponse> response) {
        // handle error condition
        if (!response.isSuccessful()) {
            Intent errorDetails = new Intent();

            errorDetails.putExtra("errorCode", response.code());
            errorDetails.putExtra("errorMessage", response.message());

            failed(errorDetails);

            return;
        }

        final ItemPublicTokenExchangeResponse ptxResponse = response.body();

        mClient.service().itemGet(new ItemGetRequest(response.body().getAccessToken())).enqueue(new Callback<ItemGetResponse>() {
            @Override
            public void onResponse(Call<ItemGetResponse> call, Response<ItemGetResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("Spender", "failed to retrieve item details");
                    getActivity().setResult(Activity.RESULT_CANCELED, new Intent()); // todo: fix this
                    return;
                }

                // examine details
                final ItemStatus status = response.body().getItem();

                Log.w("Spender", "ItemID: " + status.getItemId());
                Log.w("Spender", "InstitutionID: " + status.getInstitutionId());
                Log.w("Spender", "ItemID from token exchange: " + ptxResponse.getItemId());
                Log.w("Spender", "AccessToken: " + ptxResponse.getAccessToken());


                for (Product product : status.getBilledProducts())
                    Log.w("Spender", "Billed product: " + product.name());

                for (Product product : status.getAvailableProducts())
                    Log.w("Spender", "Available product: " + product.name());

                //mViewModel.exchangedToken(status.getItemId(), ptxResponse.getAccessToken());

                // download transaction details
                // https://support.plaid.com/hc/en-us/articles/360008271754-Transaction-dates
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                Date now = calendar.getTime();
                calendar.add(Calendar.MONTH, -1);
                Date oneMonthAgo = calendar.getTime();

                Log.e("Spender", "Waiting");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Log.e("Spender", "sleep interrupted");
                }

                Log.e("Spender", "Done waiting");

                // hardcoded because transaction pulls only happen a few times a day, and if we try and use the
                // item we just grabbed, nothing will be ready
                mClient.service().transactionsGet(
                        new TransactionsGetRequest("access-sandbox-ab544a62-f435-43cc-9d86-25ce495ecb06", oneMonthAgo, now)
                                .withAccountIds(Arrays.asList("XMNxXvq7xxub8GGJmXXnIQJEDrMqW5fd6QJyd")))
                .enqueue(new Callback<TransactionsGetResponse>() {
                    @Override
                    public void onResponse(Call<TransactionsGetResponse> call, Response<TransactionsGetResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.e("Spender", "Didn't get a response to transactions request");
                            Log.e("Spender", response.message());
                            Log.e("Spender", response.errorBody().toString());
                            Log.e("Spender", "status code: " + response.code());
                            try {
                                Log.e("Spender", "body: " + response.errorBody().string());
                            } catch (IOException e) {

                            }
                        }
                        else {
                            Log.w("Spender", "Received " + response.body().getTransactions().size() + " total transactions");

                            for (TransactionsGetResponse.Transaction t : response.body().getTransactions()) {
                                Log.w("Spender", t.toString());
                                Log.w("Spender", "AccountID: " + t.getAccountId());
                                Log.w("Spender", "name: " + t.getName());
                                Log.w("Spender", "date: " + t.getDate());
                                Log.w("Spender", "accountOwner: " + t.getAccountOwner());
                                Log.w("Spender", "transactionID: " + t.getTransactionId());
                                Log.w("Spender", "amount: " + t.getAmount());
                                Log.w("Spender", "transactionType: " + t.getTransactionType());
                                Log.w("Spender", "-----------------------------");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionsGetResponse> call, Throwable t) {
                        Log.e("Spender", "Some kind of error in getting transactions");
                    }
                });
            }

            @Override
            public void onFailure(Call<ItemGetResponse> call, Throwable t) {
                AuthFragment.this.onFailure(null, t); // todo: fix this
            }
        });

        //mViewModel.exchangedToken(response.body().getItemId(), response.body().getAccessToken());
    }

    @Override
    public void onFailure(Call<ItemPublicTokenExchangeResponse> call, Throwable t) {
        Intent errorDetails = new Intent();

        errorDetails.putExtra("errorMessage", t.toString());
        failed(errorDetails);
        return;
    }


    protected void failed(Intent failureDetails) {
        getActivity().setResult(Activity.RESULT_CANCELED, failureDetails);
        getActivity().finish();
    }
}
