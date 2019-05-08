package com.savantspender.ui.frag;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.R;
import com.savantspender.SavantSpender;
import com.savantspender.db.entity.Goal;
import com.savantspender.ui.MainActivity;
import com.savantspender.ui.frag.overview.GoalListFragment;
import com.savantspender.ui.frag.overview.OverviewMenuFragment;
import com.savantspender.util.ColorSystemDecider;
import com.savantspender.viewmodel.GoalHeaderViewModel;

import java.util.List;

public class GoalHeaderFragment extends Fragment {
    private GoalHeaderViewModel mViewModel;
    private ProgressBar mProgress;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_progress, container, false);

        mProgress = view.findViewById(R.id.goalProgress);
        mViewModel = ViewModelProviders.of(this, new GoalHeaderViewModel.Factory(getActivity().getApplication())).get(GoalHeaderViewModel.class);

        mViewModel.goals.observe(getViewLifecycleOwner(), l -> {
            updateHeaderColor(l);
        });

        mProgress.setOnClickListener(l -> {
            // launch goal list fragment
            // todo: don't launch if already in goal screen
            FragmentManager fm = getFragmentManager();
            GoalListFragment f = new GoalListFragment();

            FragmentTransaction transaction = fm.beginTransaction();

            transaction.setCustomAnimations(R.anim.enter_slide_down, R.anim.exit_slide_up, R.anim.enter_slide_down, R.anim.exit_slide_up);
            transaction.replace(R.id.action_fragment_container, f);
            transaction.addToBackStack(null);

            transaction.commit();
        });
        return view;
    }

    private void updateHeaderColor(@NonNull List<? extends Goal> goals) {
        ColorSystemDecider.GoalState worstState = ColorSystemDecider.GoalState.GOOD;

        for (Goal goal : goals) {
            ColorSystemDecider.GoalState state = ColorSystemDecider.getState(goal);

            if (state.compareTo(worstState) > 0)
                worstState = state;
        }

        mProgress.getProgressDrawable().setColorFilter(getResources().getColor(ColorSystemDecider.getColorId(worstState)), PorterDuff.Mode.SRC_IN);
    }

}
