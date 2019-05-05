package com.savantspender.ui.frag.transactions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.savantspender.R;
import com.savantspender.ui.adapter.TransactionPagerAdapter;
import com.savantspender.viewmodel.TransactionViewModel;

public class TransactionMenuFragment extends Fragment {
    private TransactionViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_transactions, container, false);

        ViewPager pager = view.findViewById(R.id.pager);

        pager.setAdapter(new TransactionPagerAdapter(getChildFragmentManager()));

        TabLayout tabs = view.findViewById(R.id.pagerTabs);

        tabs.setupWithViewPager(pager);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders
                        .of(this, new TransactionViewModel.Factory(getActivity().getApplication()))
                        .get(TransactionViewModel.class);

    }
}
