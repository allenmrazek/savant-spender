package com.savantspender.ui.frag.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.savantspender.R;
import com.savantspender.ui.MainActivity;

public class OverviewMenuFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_overview, container, false);

        view.findViewById(R.id.btnViewGoalList).setOnClickListener(l -> {
            ((MainActivity)getActivity()).TransitionTo(new GoalListFragment(), true);
        });

        view.findViewById(R.id.btnSummary).setOnClickListener(l -> {
            // todo
        });

        view.findViewById(R.id.btnCreateManual).setOnClickListener(l -> {
            CreateManualTransactionFragment mtf = new CreateManualTransactionFragment();

            mtf.show(getFragmentManager(), "dialog_createtrans");
        });
        return view;
    }
}
