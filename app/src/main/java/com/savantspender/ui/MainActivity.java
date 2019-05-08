package com.savantspender.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.savantspender.Event;
import com.savantspender.R;
import com.savantspender.SavantSpender;
import com.savantspender.db.entity.Transaction;
import com.savantspender.ui.frag.categories.CategoryMenuFragmentCategorize;
import com.savantspender.ui.frag.categories.CategoryMenuFragmentList;
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

        mViewModel = ViewModelProviders.of(this, new MainViewModel.Factory(getApplication())).get(MainViewModel.class);

        mViewModel.beginCategorize().observe(this, l -> {
            if (l.isHandled()) return;

            launchCategorizeFragment(l.getContentIfNotHandled());
        });

        mViewModel.toastMessage().observe(this, evt -> {
            if (evt.isHandled()) return;

            Toast.makeText(this, evt.getContentIfNotHandled(), Toast.LENGTH_SHORT).show();
        });


        mViewModel.createdTransaction().observe(this, l -> {
            if (l.isHandled()) return;
            l.setHandled();
            dispatchGoalUpdate();
        });

        createNotificationChannel();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(this);

        // launch the overview fragment immediately, else there won't be anything useful onscreen
        getSupportFragmentManager().beginTransaction().add(R.id.action_fragment_container, new OverviewMenuFragment()).commitNow();

        getSupportFragmentManager().addOnBackStackChangedListener(() -> onBackstackChanged());
    }


    private void dispatchGoalUpdate() {
        Log.e("Spender", "dispatching goal update");
        ((SavantSpender)getApplication()).dispatchOneTimeGoalUpdate();
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


    private void onBackstackChanged() {
        // todo
    }


    private void launchCategorizeFragment(List<Transaction> transactions) {
        if (transactions.size() == 0) {
            Log.e("Spender", "failed to launch categorize fragment: no transactions");
            return;
        }

        //TransitionTo(new CategoryMenuFragmentCategorize(transactions), true);
        TransitionTo(CategoryMenuFragmentCategorize.newInstance(transactions), true);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // todo: don't transition if already in correct state
        // todo: appropriate animation directions

        //FragmentManager.BackStackEntry previous = getSupportFragmentManager().getBackStackEntryAt(0);

        switch (menuItem.getItemId()) {
            case R.id.navigation_overview:
                TransitionTo(new OverviewMenuFragment(), true);
                return true;

            case R.id.navigation_categories:
                TransitionTo(new CategoryMenuFragmentList(), true);
                return true;

            case R.id.navigation_settings:
                TransitionTo(new SettingsMenuFragment(), true);
                return true;

            case R.id.navigation_transactions:
                TransitionTo(new TransactionMenuFragment(), true);
                return true;
        }

        return false;
    }

    public void TransitionTo(Fragment fragment, boolean addBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.action_fragment_container, fragment);

        if (addBackstack)
            transaction.addToBackStack(null);

        transaction.commit();
    }
}
