package com.savantspender.ui.frag.overview;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.savantspender.R;
import com.savantspender.SavantSpender;
import com.savantspender.viewmodel.GoalsViewModel;

public class GoalListFragment extends Fragment {
    private RecyclerView mRecycler;
    private GoalListAdapter mAdapter;
    private FloatingActionButton mAddGoalButton;
    private GoalsViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submenu_goallist, container, false);

        mRecycler = view.findViewById(R.id.lstGoals);
        mAdapter = new GoalListAdapter(getResources());

        mRecycler.setHasFixedSize(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mRecycler.setAdapter(mAdapter);

        mAddGoalButton = view.findViewById(R.id.btnOpenNewGoalDialog);
        mAddGoalButton.setOnClickListener(l -> {
            Log.w("Spender", "launching create goal fragment");

            mAddGoalButton.hide();
            CreateGoalFragment cg = new CreateGoalFragment();

            cg.show(getChildFragmentManager(), "dialog");
        });

        mViewModel = ViewModelProviders.of(getActivity(), new GoalsViewModel.Factory(getActivity().getApplication())).get(GoalsViewModel.class);
        mViewModel.dialogClosed().observe(getViewLifecycleOwner(), l -> {
            if (l.isHandled()) return;

            mAddGoalButton.show();
            l.setHandled();
        });

        mViewModel.toastMessage().observe(getViewLifecycleOwner(), l -> {
            if (l.isHandled()) return;

            Toast.makeText(getContext(), l.getContentIfNotHandled(), Toast.LENGTH_SHORT).show();
        });

        mViewModel.goals().observe(getViewLifecycleOwner(), l -> {
            mAdapter.setGoals(l);
        });

        mViewModel.dispatchUpdateGoals().observe(getViewLifecycleOwner(), l -> {
            if (!l.isHandled()) l.setHandled();
            ((SavantSpender)getActivity().getApplication()).dispatchOneTimeGoalUpdate();
        });


        return view;
    }
}
