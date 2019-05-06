package com.savantspender.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.savantspender.R;
import com.savantspender.db.entity.Transaction;
import com.savantspender.db.entity.TransactionEntity;
import com.savantspender.ui.frag.categories.CategoryMenuFragment;
import com.savantspender.ui.frag.overview.OverviewMenuFragment;
import com.savantspender.ui.frag.settings.SettingsMenuFragment;
import com.savantspender.ui.frag.transactions.TransactionMenuFragment;
import com.savantspender.viewmodel.MainViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mViewModel.beginCategorize().observe(this, l -> {
            if (l.isHandled()) return;

            launchCategorizeFragment(l.getContentIfNotHandled());
        });

        createNotificationChannel();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(this);

        // launch the overview fragment immediately, else there won't be anything useful onscreen
        getSupportFragmentManager().beginTransaction().add(R.id.action_fragment_container, new OverviewMenuFragment()).commitNow();


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


    private void launchCategorizeFragment(List<Transaction> transactions) {
        if (transactions.size() == 0) {
            Log.e("Spender", "failed to launch categorize fragment: no transactions");
            return;
        }

        // todo
        Log.e("Spender", "launch categorize fragment now");
        TransitionTo(new CategoryMenuFragment(transactions));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // todo: don't transition if already in correct state
        // todo: appropriate animation directions

        switch (menuItem.getItemId()) {
            case R.id.navigation_overview:
                TransitionTo(new OverviewMenuFragment());
                return true;

            case R.id.navigation_categories:
                TransitionTo(new CategoryMenuFragment());
                return true;

            case R.id.navigation_settings:
                TransitionTo(new SettingsMenuFragment());
                return true;

            case R.id.navigation_transactions:
                TransitionTo(new TransactionMenuFragment());
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
