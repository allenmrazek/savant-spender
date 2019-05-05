package com.savantspender.ui.frag;

import android.os.Bundle;
import android.util.Log;
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

public class TransactionMenuFragment extends Fragment {
    private TransactionViewModel mViewModel;
    private RecyclerView mRecycler;
    private TransactionViewAdapter mAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_transactions, container, false);

        mRecycler = view.findViewById(R.id.transactions_view);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mAdapter = new TransactionViewAdapter();
        mRecycler.setAdapter(mAdapter);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("Spender", "transaction fragment activity created");
        mViewModel = ViewModelProviders
                        .of(this, new TransactionViewModel.Factory(getActivity().getApplication()))
                        .get(TransactionViewModel.class);

        mViewModel.uncataloggedTransactions().observe(getViewLifecycleOwner(), i -> {
            mAdapter.submitData(i);
            mAdapter.notifyDataSetChanged();
        });
    }
}
