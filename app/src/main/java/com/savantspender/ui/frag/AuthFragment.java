package com.savantspender.ui.frag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.savantspender.R;
import com.savantspender.viewmodel.LinkViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthFragment extends Fragment implements Observer<LinkViewModel.PlaidLink_ItemData>, Callback<ItemPublicTokenExchangeResponse> {
    private LinkViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(LinkViewModel.class);
        mViewModel.getItemData().observe(this, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        return view;
    }

    @Override
    public void onChanged(LinkViewModel.PlaidLink_ItemData item) {

        // this is the bit that should normally be protected behind a remote server
        PlaidClient client = PlaidClient.newBuilder()
                .clientIdAndSecret("5c54506a47679a00117ebada", "7be0aefd21b235efcb5717101969ca")
                .publicKey("efebb105ab905b6e6cbe0a12e4689b") // optional. only needed to call endpoints that require a public key
                .sandboxBaseUrl() // or equivalent, depending on which environment you're calling into
                .build();

        client.service().itemPublicTokenExchange(new ItemPublicTokenExchangeRequest(item.getPublicToken()))
                .enqueue(this);
    }

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

        mViewModel.exchangedToken(response.body().getItemId(), response.body().getAccessToken());
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
