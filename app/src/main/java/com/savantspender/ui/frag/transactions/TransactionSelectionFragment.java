package com.savantspender.ui.frag.transactions;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.savantspender.R;
import com.savantspender.ui.frag.transactions.adapter.TransactionViewAdapter;
import com.savantspender.viewmodel.TransactionViewModel;

public abstract class TransactionSelectionFragment extends Fragment {
    protected TransactionViewModel mViewModel;
    protected RecyclerView mRecyclerView;
    protected TransactionViewAdapter mAdapter;
    protected FloatingActionButton mFloatingButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_submenu_transactionlist, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);


        mAdapter = new TransactionViewAdapter();

        mRecyclerView = view.findViewById(R.id.transactionList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);


        mFloatingButton = view.findViewById(R.id.btnFloating);

        toggleFloatingButton(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity(), new TransactionViewModel.Factory(getActivity().getApplication())).get(TransactionViewModel.class);

        mFloatingButton.setOnClickListener(v -> onFloatingButtonClicked());

        initObservers();
    }

    protected void toggleFloatingButton(boolean visible) {
        if (visible)
            mFloatingButton.show();
        else mFloatingButton.hide();
    }

    protected abstract void initObservers();
    protected abstract void onFloatingButtonClicked();
}
