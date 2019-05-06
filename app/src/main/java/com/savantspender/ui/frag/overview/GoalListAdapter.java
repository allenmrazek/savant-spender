package com.savantspender.ui.frag.overview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.savantspender.R;

public class GoalListAdapter extends RecyclerView.Adapter<GoalListAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_goal, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // todo
    }

    @Override
    public int getItemCount() {
        return 100;
    }
    // todo


    static class ViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgress;
        private TextView mName;
        private TextView mTarget;
        private TextView mPrediction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mProgress = itemView.findViewById(R.id.goalProgress);
            mName = itemView.findViewById(R.id.txtGoalName);
            mTarget = itemView.findViewById(R.id.txtGoalTargetAmount);
            mPrediction = itemView.findViewById(R.id.txtPredictionAmount);
        }
    }
}
