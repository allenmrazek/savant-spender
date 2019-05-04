package com.savantspender.worker;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class TestWorker extends Worker {
    public TestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() { // is already on a background thread
        String tags;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tags = String.join(",", getTags());
        } else tags = TextUtils.join(",", getTags());

        Log.e("Spender", "**************** finished work *********************: id = " + getId() + "; tag = " + tags);

        Result r = Result.success();

        return r;
    }
}
