package com.savantspender.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.GoalTagsEntity;
import com.savantspender.db.entity.TagEntity;
import com.savantspender.db.entity.TransactionEntity;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpdateGoalsWorker extends Worker {
    private static final int MIN_DAYS_DATA = 5;

    private AppDatabase mDatabase;

    public UpdateGoalsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("Spender", "updating goals now ...");

        SavantSpender spender = (SavantSpender) getApplicationContext();

        mDatabase = spender.getDatabase();

        for (GoalEntity goal : mDatabase.goalDao().getAllSync())
            updateGoal(goal);

        return Result.failure();
    }


    private void updateGoal(GoalEntity goal) {
        Calendar monthStart = Calendar.getInstance();
        //Calendar monthEnd = Calendar.getInstance();


        monthStart.set(Calendar.DATE, 1);
        //monthEnd.set(Calendar.DATE, monthEnd.getActualMaximum(Calendar.DATE));

        Calendar queryStart = (Calendar)monthStart.clone();

        // if we're not very far into the month yet, the regression model won't
        // be very accurate. Use historical data to improve accuracy
        if (daysBetween(monthStart.getTime(), Calendar.getInstance().getTime() /* current date */) < MIN_DAYS_DATA) {
            queryStart.add(Calendar.DATE, -MIN_DAYS_DATA);
        }

        // grab tags for this goal
        List<GoalTagsEntity> tags = mDatabase.goalTagDao().getTagsFor(goal.id);

        Pair<Double, Double> prediction = predictEndAmount(goal, tags, queryStart.getTime(), monthStart.getTime());

        Log.e("Spender", "calculated total of " + prediction.first + " and predicting " + prediction.second);
    }


    private Pair<Double /* amount */, Double /* prediction */> predictEndAmount(GoalEntity goal, List<GoalTagsEntity> tags, Date queryStart, Date monthStart) {
        Calendar monthEnd = Calendar.getInstance();
        monthEnd.set(Calendar.DATE, monthEnd.getActualMaximum(Calendar.DATE));

        List<Integer> tagIds = new ArrayList<>();

        for (GoalTagsEntity gte : tags)
            tagIds.add(gte.tagId);

        // grab list of all transactions which are tagged with any of the specified tags
        List<TransactionEntity> transactions = mDatabase.cataloggedDao().getTransactions(queryStart, monthEnd.getTime(), tagIds);

        // start building model. Remember to exclude historical data from the goal's amount,
        // because those are last month's transactions
        double total = 0.0;
        double accumulated = 0.0;

        SimpleRegression regression = new SimpleRegression(false);

        for (TransactionEntity transaction : transactions) {
            if (!transaction.postDate.before(monthStart)) {
                total += transaction.amount;
            }

            accumulated += transaction.amount;

            regression.addData((double)transaction.postDate.getTime() - queryStart.getTime(), accumulated);
        }

        return new Pair<>(total, regression.predict(monthEnd.getTime().getTime() - queryStart.getTime()));
    }


    // dirty solution
    private static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }
}
