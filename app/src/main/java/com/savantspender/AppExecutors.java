package com.savantspender;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private final Executor mDiskIO;
    private final Executor mNetworkIO;
    private final Executor mMainThread;

    public AppExecutors() {
        mDiskIO = Executors.newSingleThreadExecutor();
        mNetworkIO = Executors.newSingleThreadExecutor();
        mMainThread = new MainThreadExecutor();
    }


    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
