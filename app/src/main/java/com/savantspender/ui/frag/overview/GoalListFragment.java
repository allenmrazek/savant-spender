package com.savantspender.ui.frag.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.savantspender.R;

public class GoalListFragment extends Fragment {
    private RecyclerView mRecycler;
    private GoalListAdapter mAdapter;
    private FloatingActionButton mAddGoalButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submenu_goallist, container, false);

        mRecycler = view.findViewById(R.id.lstGoals);
        mAdapter = new GoalListAdapter();

        mRecycler.setHasFixedSize(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);

        mAddGoalButton = view.findViewById(R.id.btnAcceptAddGoal);
        mAddGoalButton.setOnClickListener(l -> {
            mAddGoalButton.hide();
            CreateGoalFragment cg = new CreateGoalFragment();

            cg.show(getChildFragmentManager(), "dialog");
        });

        return view;
    }
}
