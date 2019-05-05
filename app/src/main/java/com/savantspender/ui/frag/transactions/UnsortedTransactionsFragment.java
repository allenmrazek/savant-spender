package com.savantspender.ui.frag.transactions;

public class UnsortedTransactionsFragment extends TransactionSelectionFragment {
    @Override
    protected void initObservers() {
        mViewModel.unsortedTransactions().observe(getViewLifecycleOwner(), t -> mAdapter.submitData(t) );
    }
}
