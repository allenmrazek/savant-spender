package com.savantspender.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.ui.frag.AuthFragment;
import com.savantspender.ui.frag.LinkFragment;
import com.savantspender.viewmodel.LinkViewModel;

public class LinkActivity extends AppCompatActivity {
    public static final int REQUEST_NEW_LINK = 1; // todo: add more for patch / update mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinkViewModel lvm = ViewModelProviders
                .of(this, new LinkViewModel.Factory(getApplication()))
                .get(LinkViewModel.class);

        // launch link
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, new LinkFragment())
                .commit();

        // once authorization begins, swap to the auth fragment so the user knows
        // we're busy. Authorization continues in background on separate thread
        lvm.authorization().observe(this, i -> {
            if (i.isHandled()) return;

            // launch authorization fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new AuthFragment())
                    .commit();
        });

        lvm.completion().observe(this, r -> {
            if (r.isHandled()) return;

            Pair<Integer, Intent> results = r.getContentIfNotHandled();

            if (results == null) return;

            setResult(results.first, results.second);
            finish();
        });
    }
}
