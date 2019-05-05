package com.savantspender.ui.frag.transactions;

public class SortedTransactionsFragment extends TransactionSelectionFragment {

    @Override
    protected void initObservers() {
        mViewModel.sortedTransactions().observe(getViewLifecycleOwner(), t -> mAdapter.submitData(t) );
    }


}
