package com.savantspender.ui.frag.transactions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.savantspender.R;

public class SortedTransactionsFragment extends TransactionSelectionFragment {

    @Override
    protected void initObservers() {
        mViewModel.sortedTransactions().observe(getViewLifecycleOwner(), t -> mAdapter.submitData(t) );

        mAdapter.selectionChanged().observe(getViewLifecycleOwner(), num -> {
            if (num.isHandled()) return;

            toggleFloatingButton(num.getContentIfNotHandled() > 0);
        });


    }

    @Override
    protected void onFloatingButtonClicked() {
        mViewModel.doUnsortTransactions(mAdapter.getSelected());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        mFloatingButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_trash));

        return view;
    }
}
