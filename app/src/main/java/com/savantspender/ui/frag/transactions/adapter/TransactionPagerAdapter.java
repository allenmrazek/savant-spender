package com.savantspender.ui.frag.transactions.adapter;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.savantspender.ui.frag.transactions.SortedTransactionsFragment;
import com.savantspender.ui.frag.transactions.UnsortedTransactionsFragment;

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
        if (position < 0 || position > 1)
            Log.e("Spender", "unrecognized transaction page: " + position);

        switch (position) {
            case 0: // unsorted
            default:
                return new UnsortedTransactionsFragment();

            case 1: // sorted
                return new SortedTransactionsFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Unsorted";
            case 1:
                return "Sorted";

            default:
                return "Unknown";
        }
    }
}
