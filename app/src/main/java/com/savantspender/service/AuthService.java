package com.savantspender.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.savantspender.BuildConfig;
import com.savantspender.R;
import com.savantspender.SavantSpender;

public class AuthService extends Service {
    private int startId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        startForeground(startId, createNotification());

        Log.e("Spender", "AuthService started");

        ((SavantSpender)getApplication()).getExecutors().networkIO().execute(() -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {

            }

            Log.e("Spender","finished sleeping");

            stopForeground(true);
            stopSelfResult(startId);
        });

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // do not permit binding to outside services
    }

    // foreground services require notifications
    protected Notification createNotification() {
        final String contentTitle = "Authenticating login details...";

        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification= new Notification.Builder(this, "default")
                    .setContentTitle(contentTitle)
                    .setSmallIcon(R.drawable.ic_query_builder_black_24dp)
                    .build();
        } else {
            notification = new Notification.Builder(this)
                    .setContentTitle(contentTitle)
                    .setSmallIcon(R.drawable.ic_query_builder_black_24dp)
                    .build();
        }

        return notification;
    }

}
