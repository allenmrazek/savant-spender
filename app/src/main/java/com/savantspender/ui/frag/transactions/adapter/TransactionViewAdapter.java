package com.savantspender.ui.frag.transactions.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.savantspender.Event;
import com.savantspender.R;
import com.savantspender.db.entity.Transaction;

import java.util.LinkedList;
import java.util.List;

public class TransactionViewAdapter extends RecyclerView.Adapter<TransactionViewAdapter.ViewHolder> {
    private List<? extends Transaction> mData = new LinkedList<>();
    private final MutableLiveData<Event<Integer>> mSelectionsChanged = new MutableLiveData<>();
    private int mSelections = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_transaction, parent, false);
        ViewHolder vh = new ViewHolder(view, this);

        view.setOnClickListener(i -> onViewHolderClick(vh));
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction trans = mData.get(position);

        holder.txtTransactionName.setText(trans.getName());
        holder.txtTransactionPrice.setText(Double.toString(trans.getAmount()));
        holder.bind(trans.isSelected());
    }

    private void onViewHolderClick(ViewHolder whichVh) {
        int pos = whichVh.getAdapterPosition();

        // toggle selection
        boolean newState = !mData.get(pos).isSelected();

        mData.get(pos).setSelected(newState);
        whichVh.bind(newState);

        mSelections += newState ? 1 : -1;
        mSelectionsChanged.postValue(new Event<Integer>(new Integer(mSelections)));
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void submitData(@NonNull List<? extends Transaction> transactions) {
        mData = transactions;
        mSelections = 0;

        notifyDataSetChanged();

        mSelectionsChanged.postValue(new Event<Integer>(new Integer(0)));
    }


    public LiveData<Event<Integer>> selectionChanged() {
        return mSelectionsChanged;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTransactionName;
        private final TextView txtTransactionPrice;
        private final TextView txtTransactionDate;
        private final ImageView btnViewDetails;


        public ViewHolder(@NonNull View itemView, @NonNull TransactionViewAdapter adapter) {
            super(itemView);

            txtTransactionName = itemView.findViewById(R.id.txtTransName);
            txtTransactionPrice = itemView.findViewById(R.id.txtTransPrice);
            txtTransactionDate = itemView.findViewById(R.id.txtTransDate);
            btnViewDetails = itemView.findViewById(R.id.btnDetails);
            btnViewDetails.setOnClickListener(i -> Log.w("Spender", "view details here")); // todo
        }

        public void bind(boolean isSelected) {
            itemView.setActivated(isSelected);

            if (isSelected)
                itemView.setBackgroundColor(Color.RED);
            else itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
