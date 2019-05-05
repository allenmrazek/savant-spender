package com.savantspender.ui.frag;

import android.app.Activity;
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
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.savantspender.BuildConfig;
import com.savantspender.R;
import com.savantspender.ui.LinkActivity;
import com.savantspender.ui.MainActivity;
import com.savantspender.viewmodel.SettingsViewModel;
import com.savantspender.worker.DownloadTransactionsWorker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends Fragment {
    private static final int TRANSACTION_DELAY_ON_LINK = 5; // in minutes

    private SettingsViewModel mViewModel;

    private View mLinkButton;
    private View mDeleteAccountButton;
    private View mDeleteDbButton;
    private View mGenerateTransactionButton;
    private View mUpdateNowButton;

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

        // delete account button -> launches a new fragment (hosted by MainActivity)
        mDeleteAccountButton = view.findViewById(R.id.btnDeleteAccount);
        mDeleteAccountButton.setOnClickListener(v -> {
            ((MainActivity)getActivity()).TransitionTo(new DeleteItemsFragment());
        });

        // update button -> downloads all available transactions
        mUpdateNowButton = view.findViewById(R.id.btnUpdateNow);
        mUpdateNowButton.setOnClickListener(v -> scheduleTransactionUpdate(0));

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
        String message;

        if (resultCode != Activity.RESULT_OK) {
            // something went wrong!
            // todo: better error handling
            message = getResources().getString(R.string.toast_error_auth);

            if (data != null) {
                data.getStringExtra("errorCode");
                data.getStringExtra("errorMessage");
            }

            Toast.makeText(getActivity().getApplicationContext(), R.string.toast_error_auth, Toast.LENGTH_SHORT).show();

        } else {
            message = getResources().getString(R.string.toast_link_success);

            if (data != null) {
                String institutionName = data.getStringExtra("institutionName");

                if (institutionName != null) {
                    message = getResources().getString(R.string.toast_link_success_instname, institutionName);
                }
            }
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

            // schedule an update in a bit: the data takes time to be ready (and even this call
            // has a high probability of failure)
            scheduleTransactionUpdate(TRANSACTION_DELAY_ON_LINK);
        }
    }


    private void scheduleTransactionUpdate(int minutes) {
        WorkRequest downloadTransactions =
                new OneTimeWorkRequest.Builder(DownloadTransactionsWorker.class)
                        .setConstraints(new Constraints.Builder().build())
                        .addTag(getResources().getString(R.string.work_dl_trans_s))
                        .setInitialDelay(minutes, TimeUnit.MINUTES)
                        .build();

        WorkManager.getInstance().enqueue(downloadTransactions);
    }

}
