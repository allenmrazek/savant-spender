package com.savantspender.ui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.savantspender.R;
import com.savantspender.ui.adapter.TransactionViewAdapter;
import com.savantspender.viewmodel.TransactionViewModel;

public class TransactionUnsortedFragment extends Fragment {
    private TransactionViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private TransactionViewAdapter mAdapter;
    private SelectionTracker<Long> mSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submenu_transactionlist, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);


        mAdapter = new TransactionViewAdapter();
        mAdapter.setHasStableIds(true);

        mRecyclerView = view.findViewById(R.id.transactionList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


        mSelected = new SelectionTracker.Builder<>(
                "unsorted_transaction_list",
                mRecyclerView,
                new TransactionViewAdapter.TransactionKeyProvider(mRecyclerView),
                new TransactionViewAdapter.TransactionItemDetailsLookup(mRecyclerView),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectAnything())
        .build();

        mAdapter.setSelectionTracker(mSelected);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity(), new TransactionViewModel.Factory(getActivity().getApplication())).get(TransactionViewModel.class);

        mViewModel.uncataloggedTransactions().observe(getViewLifecycleOwner(), t -> mAdapter.submitData(t) );
    }
}
