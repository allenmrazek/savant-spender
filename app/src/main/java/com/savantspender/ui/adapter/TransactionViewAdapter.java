package com.savantspender.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.savantspender.R;

public class TransactionViewAdapter extends RecyclerView.Adapter<TransactionViewAdapter.ViewHolder> {
    private String[] mData;

    public TransactionViewAdapter() {
        mData = new String[] { "Hello", "World" };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_transaction, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTransactionName.setText(mData[position]);
        Log.e("Spender", "Binding view on pos " + position);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTransactionName;
        private TextView txtTransactionPrice;
        private TextView txtTransactionDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTransactionName = itemView.findViewById(R.id.txtTransName);
            txtTransactionPrice = itemView.findViewById(R.id.txtTransPrice);
            txtTransactionDate = itemView.findViewById(R.id.txtTransDate);
        }
    }
}
