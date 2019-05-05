package com.savantspender.ui.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.savantspender.ui.frag.transactions.TransactionUnsortedFragment;

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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Page 0";
            case 1:
                return "Page 1";

            default:
                return "Unknown";
        }
    }
}
