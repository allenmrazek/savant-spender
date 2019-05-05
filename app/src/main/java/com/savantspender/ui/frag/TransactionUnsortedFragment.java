package com.savantspender.ui.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.savantspender.R;
import com.savantspender.ui.adapter.TransactionViewAdapter;
import com.savantspender.viewmodel.TransactionViewModel;

public class TransactionUnsortedFragment extends Fragment {
    private TransactionViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private TransactionViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submenu_transactionlist, container, false);

        mRecyclerView = view.findViewById(R.id.transactionList);
        mAdapter = new TransactionViewAdapter();
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity(), new TransactionViewModel.Factory(getActivity().getApplication())).get(TransactionViewModel.class);

        mViewModel.uncataloggedTransactions().observe(getViewLifecycleOwner(), t -> mAdapter.submitData(t) );
    }
}
