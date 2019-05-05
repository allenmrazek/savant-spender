package com.savantspender.ui.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.savantspender.ui.frag.TransactionUnsortedFragment;

public class TransactionPagerAdapter extends FragmentPagerAdapter {

    public TransactionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // todo
            case 1:
                // todo;

            default:
                return new TransactionUnsortedFragment();
        }
    }
}
