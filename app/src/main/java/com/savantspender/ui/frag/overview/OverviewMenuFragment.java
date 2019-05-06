package com.savantspender.ui.frag.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.savantspender.R;
import com.savantspender.ui.MainActivity;

public class OverviewMenuFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_overview, container, false);

        view.findViewById(R.id.btnViewGoalList).setOnClickListener(l -> {
            ((MainActivity)getActivity()).TransitionTo(new GoalListFragment(), true);
        });

        return view;
    }
}
