package com.savantspender.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.savantspender.R;
import com.savantspender.TransactionActivity;
import com.savantspender.ui.frag.CategoryFragment;
import com.savantspender.ui.frag.OverviewFragment;
import com.savantspender.ui.frag.SettingsFragment;
import com.savantspender.ui.frag.TransactionFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// TODO: put all activities into fragments, share bottom navigation bar between them, use
// fragment manager animations to swap
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        createNotificationChannel();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(this);

        Button b = findViewById(R.id.btn_link);

        b.setOnClickListener(view -> {
            startActivityForResult(new Intent(this, LinkActivity.class), LinkActivity.REQUEST_NEW_LINK);
        });

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LinkActivity.REQUEST_NEW_LINK:
                onFinishedLink(resultCode, data);
                break;

            default:
                Log.e("Spender", "Unrecognized activity request code: " + requestCode);
                break;
        }
    }

    protected void onFinishedLink(int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            // something went wrong!
            Toast.makeText(getApplicationContext(), R.string.toast_error_auth, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.toast_link_success, Toast.LENGTH_SHORT).show();
        }

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
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // todo: don't transition if already in correct state

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

    private void TransitionTo(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.action_fragment_container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
