package com.savantspender.ui.frag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.BuildConfig;
import com.savantspender.R;
import com.savantspender.ui.LinkActivity;
import com.savantspender.ui.MainActivity;
import com.savantspender.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends Fragment {
    private SettingsViewModel mViewModel;

    private View mLinkButton;
    private View mDeleteAccountButton;
    private View mDeleteDbButton;
    private View mGenerateTransactionButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders
                .of(this, new SettingsViewModel.Factory(getActivity().getApplication()))
                .get(SettingsViewModel.class);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // display these special developer-only buttons for debugging purposes
        if (BuildConfig.DEBUG) {
            ArrayList<Integer> debugButtons = new ArrayList<Integer>(Arrays.asList(R.id.btnDeleteDatabase, R.id.btnUpdateNow, R.id.btnRandomTransaction));

            for (Integer id : debugButtons)
                view.findViewById(id).setVisibility(View.VISIBLE);
        }

        // delete database button
        mDeleteDbButton = view.findViewById(R.id.btnDeleteDatabase);
        mDeleteDbButton.setOnClickListener(v -> {
            mViewModel.onDeleteDatabaseClicked();
        });

        // random transaction button
        mGenerateTransactionButton = view.findViewById(R.id.btnRandomTransaction);
        mGenerateTransactionButton.setOnClickListener(v ->
                mViewModel.onGenerateRandomTransactionClicked()
        );

        // link account button
        mLinkButton = view.findViewById(R.id.btnLinkNew);
        mLinkButton.setOnClickListener( v -> {
            // todo: disable link button?
            startActivityForResult(new Intent(this.getContext(), LinkActivity.class), LinkActivity.REQUEST_NEW_LINK);
        });

        // delete account button
        mDeleteAccountButton = view.findViewById(R.id.btnDeleteAccount);
        mDeleteAccountButton.setOnClickListener(v -> {
            ((MainActivity)getActivity()).TransitionTo(new DeleteItemsFragment());
        });

        mViewModel.toastMessages().observe(getViewLifecycleOwner(), m -> {
            if (m.isHandled()) return;
            Toast.makeText(getContext(), m.getContentIfNotHandled(), Toast.LENGTH_SHORT);
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LinkActivity.REQUEST_NEW_LINK:
                onFinishedLink(resultCode, data);
                break;

            default:
                Log.e("Spender", "Unrecognized activity request code: " + requestCode);
                break;
        }
    }

    protected void onFinishedLink(int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            // something went wrong!
            // todo: better error handling
            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_error_auth, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_link_success, Toast.LENGTH_SHORT).show();
        }

    }
}
