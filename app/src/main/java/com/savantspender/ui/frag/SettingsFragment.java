package com.savantspender.ui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.BuildConfig;
import com.savantspender.R;
import com.savantspender.viewmodel.SettingsViewModel;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends Fragment {
    private SettingsViewModel mViewModel;

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


        view.findViewById(R.id.btnDeleteDatabase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.onDeleteDatabaseClicked();
            }
        });

        return view;
    }


}
