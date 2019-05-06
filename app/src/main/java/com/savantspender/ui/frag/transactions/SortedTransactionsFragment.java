package com.savantspender.ui.frag.transactions;

import android.util.Log;

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

    public void transactionsWereSorted() {
        // todo: remove this
        Log.e("Spender", "transactions were sorted");
    }

}
