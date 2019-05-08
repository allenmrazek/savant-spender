package com.savantspender.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class UpdateGoalsWorker extends Worker {
    private AppDatabase mDatabase;

    public UpdateGoalsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("Spender", "updating goals now ...");

        SavantSpender spender = (SavantSpender) getApplicationContext();


        SimpleRegression simpleRegression = new SimpleRegression(true);

        // passing data to the model
        // model will be fitted automatically by the class
        simpleRegression.addData(new double[][] {
                {1, 10},
                {2, 20},
                {3, 30},
                {4, 40},
                {5, 50}
        });

        // querying for model parameters
        Log.e("Spender", "slope = " + simpleRegression.getSlope());
        Log.e("Spender", "intercept = " + simpleRegression.getIntercept());

        // trying to run model for unknown data
        Log.e("Spender", "prediction for 6 = " + simpleRegression.predict(6));

        return Result.failure();
    }
}
