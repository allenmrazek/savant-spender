package com.savantspender.ui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.R;
import com.savantspender.viewmodel.GoalHeaderViewModel;

public class GoalHeaderFragment extends Fragment {
    private GoalHeaderViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_progress, container, false);

        mViewModel = ViewModelProviders.of(this, new GoalHeaderViewModel.Factory(getActivity().getApplication())).get(GoalHeaderViewModel.class);
        
        return view;
    }
}
