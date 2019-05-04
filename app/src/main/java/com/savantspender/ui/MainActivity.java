package com.savantspender.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.savantspender.R;
import com.savantspender.service.AuthService;
import com.savantspender.ui.frag.CategoryFragment;
import com.savantspender.ui.frag.OverviewFragment;
import com.savantspender.ui.frag.SettingsFragment;
import com.savantspender.ui.frag.TransactionFragment;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        createNotificationChannel();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(this);

//        Button b = findViewById(R.id.btn_link);

//        b.setOnClickListener(view -> {
//            startActivityForResult(new Intent(this, LinkActivity.class), LinkActivity.REQUEST_NEW_LINK);
//        });
//

        // launch the overview fragment immediately, else there won't be anything useful onscreen
        getSupportFragmentManager().beginTransaction().add(R.id.action_fragment_container, new OverviewFragment()).commitNow();

        // temp: testing notifications
        if (Build.VERSION.SDK_INT >= 26) {

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager.getNotificationChannel("default") == null) {
                NotificationChannel channel = new NotificationChannel("default",
                        "Errors",
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription("Errors that Savant Spender encounters");
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
//                .setContentTitle("Test title")
//                .setContentText("Test text here")
//                .setSmallIcon(R.drawable.ic_query_builder_black_24dp)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
//        notificationManager.notify(1234, builder.build());

        // bad plan: activities destroyed on screen change, starts service extra times
        // but otherwise is correct way to start foreground service
//        ComponentName service = startForegroundService(
//                new Intent(getApplicationContext(), AuthService.class));

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channel_id), name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // note: safe for this to be called multiple times; does nothing once the channel
            // has been created

            Log.i("Spender", "Created notification channel");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // todo: don't transition if already in correct state
        // todo: appropriate animation directions

        switch (menuItem.getItemId()) {
            case R.id.navigation_overview:
                TransitionTo(new OverviewFragment());
                return true;

            case R.id.navigation_categories:
                TransitionTo(new CategoryFragment());
                return true;

            case R.id.navigation_settings:
                TransitionTo(new SettingsFragment());
                return true;

            case R.id.navigation_transactions:
                TransitionTo(new TransactionFragment());
                return true;
        }

        return false;
    }

    public void TransitionTo(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.action_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
