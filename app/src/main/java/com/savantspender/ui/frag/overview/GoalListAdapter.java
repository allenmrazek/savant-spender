package com.savantspender.ui.frag.overview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.savantspender.R;
import com.savantspender.db.entity.Goal;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class GoalListAdapter extends RecyclerView.Adapter<GoalListAdapter.ViewHolder> {
    private List<? extends Goal> mGoals = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_goal, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal g = mGoals.get(position);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(g.getAmount());

        holder.mName.setText(g.getName());
        holder.mTarget.setText(moneyString);
    }


    @Override
    public int getItemCount() {
        return mGoals.size();
    }


    public void setGoals(List<? extends Goal> goals) {
        mGoals = goals;
        notifyDataSetChanged();
    }



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
