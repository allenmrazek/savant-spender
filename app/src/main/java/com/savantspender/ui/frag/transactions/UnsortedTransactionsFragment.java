package com.savantspender.ui.frag.transactions;

import android.content.Context;

import androidx.lifecycle.ViewModelProviders;

import com.savantspender.viewmodel.MainViewModel;

public class UnsortedTransactionsFragment extends TransactionSelectionFragment {
    @Override
    protected void initObservers() {
        mViewModel.unsortedTransactions().observe(getViewLifecycleOwner(), t -> mAdapter.submitData(t) );
        mAdapter.selectionChanged().observe(getViewLifecycleOwner(), num -> {
            if (num.isHandled()) return;
            num.setHandled();
            updateFloatingButtonVisibility();
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    protected void updateFloatingButtonVisibility() {
        toggleFloatingButton(mAdapter.getSelected().size() > 0);
    }


    @Override
    protected void onFloatingButtonClicked() {
        ViewModelProviders.of(getActivity()).get(MainViewModel.class).enterCategorizeMode(mAdapter.getSelected());
    }
}
