package com.savantspender.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.GoalTagsEntity;
import com.savantspender.db.entity.TransactionEntity;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class UpdateGoalsWorker extends Worker {
    private AppDatabase mDatabase;

    public UpdateGoalsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SavantSpender spender = (SavantSpender) getApplicationContext();

        mDatabase = spender.getDatabase();

        for (GoalEntity goal : mDatabase.goalDao().getAllSync())
            updateGoal(goal);

        return Result.success();
    }

    private void updateGoal(GoalEntity goal) {
        Calendar monthStart = Calendar.getInstance();
        Calendar monthEnd = Calendar.getInstance();

        int daysThisMonth = monthEnd.getActualMaximum(DATE);


        monthStart.set(DAY_OF_MONTH, 1);
        monthStart.set(HOUR_OF_DAY, 0);
        monthStart.set(MINUTE, 0);
        monthStart.set(SECOND, 0);

        monthEnd.set(DATE, daysThisMonth);
        monthEnd.set(HOUR_OF_DAY, 0);
        monthEnd.set(MINUTE, 0);
        monthEnd.set(SECOND, 0);

        // we can't tell exact times, only dates
        // in order to make the linreg work, we'll have to predict based on the total for
        // each day of the month
        ArrayList<Double> buckets = new ArrayList<>();
        double total = 0.0;

        for (int i = 0; i < daysThisMonth; ++i)
            buckets.add(0.0);

        // grab list of all transactions which are tagged with any of the specified tags
        List<TransactionEntity> transactions = mDatabase.cataloggedDao().getTransactions(monthStart.getTime(), monthEnd.getTime(), getTagIds(goal));

        // sort each transaction into its corresponding bucket
        for (TransactionEntity transaction : transactions) {
            Calendar transDate = Calendar.getInstance();

            transDate.setTime(transaction.postDate);

            int bucket = transDate.get(DATE) - 1; // days is 1-based, array 0-based

            buckets.set(bucket, buckets.get(bucket) + transaction.getAmount());

            total += transaction.getAmount();
        }

        // build regression model
        SimpleRegression regression = new SimpleRegression(false);
        double accumulatedTotal = 0.0;

        // make sure we count only up to the current date: beyond this, the buckets are probably
        // zero and should NOT be included in the regression
        for (int i = 0; i < Calendar.getInstance().get(DATE); ++i) {
            if (buckets.get(i) < 0.01) continue;

            regression.addData((double) i, buckets.get(i) + accumulatedTotal);
            accumulatedTotal += buckets.get(i);
        }

        goal.amount = total;
        goal.predicted = regression.predict((double)(daysThisMonth - 1));

        if (goal.predicted < goal.amount || Double.isNaN(goal.predicted))
            goal.predicted = goal.amount;

        goal.rsquared = regression.getRSquare();
        goal.rvalue = regression.getR();

        if (Double.isNaN(goal.rsquared)) goal.rsquared = 0.0;
        if (Double.isNaN(goal.rvalue)) goal.rvalue = 0.0;

        Log.e("Spender", "prediction for " + goal.name + " is " + goal.predicted);

        mDatabase.goalDao().update(goal);
    }


    private List<Integer> getTagIds(GoalEntity goal) {
        ArrayList<Integer> ids = new ArrayList<>();

        // grab tags for this goal
        List<GoalTagsEntity> tags = mDatabase.goalTagDao().getTagsFor(goal.id);

        for (GoalTagsEntity t : tags)
            ids.add(t.tagId);

        return ids;
    }
}
