package com.savantspender.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.ui.frag.AuthFragment;
import com.savantspender.ui.frag.LinkFragment;
import com.savantspender.viewmodel.LinkViewModel;

public class LinkActivity extends AppCompatActivity implements Observer<LinkViewModel.PlaidLink_ItemData> {
    public static final int REQUEST_NEW_LINK = 1; // todo: add more for patch / update mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinkViewModel lvm = ViewModelProviders
                .of(this)
                .get(LinkViewModel.class);

        // observe changes in item data (specifically: whether we got any or not)
        lvm.getItemData().observe(this, this);

        // observe changes that would result in this activity ending
        lvm.getCompletionIntent().observe(this, i -> {
            setResult(i.first, i.second != null ? i.second : new Intent());
            finish();
        });

        // launch link
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, new LinkFragment())
                .commit();
    }


    // Successfully retrieved public token and item id(s), now they need to be exchanged
    // for access tokens. Launch auth fragment
    @Override
    public void onChanged(LinkViewModel.PlaidLink_ItemData plaidLink_itemData) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new AuthFragment())
                .commit();
    }
}
