package com.savantspender.ui.frag.transactions;

public class UnsortedTransactionsFragment extends TransactionSelectionFragment {
    @Override
    protected void initObservers() {
        mViewModel.unsortedTransactions().observe(getViewLifecycleOwner(), t -> mAdapter.submitData(t) );
        mAdapter.selectionChanged().observe(getViewLifecycleOwner(), num -> {
            if (num.isHandled()) return;

            toggleFloatingButton(num.getContentIfNotHandled() > 0);
        });
    }

    @Override
    protected void onFloatingButtonClicked() {

    }
}
