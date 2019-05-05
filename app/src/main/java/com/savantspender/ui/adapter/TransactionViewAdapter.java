package com.savantspender.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.savantspender.R;
import com.savantspender.db.entity.Transaction;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TransactionViewAdapter extends RecyclerView.Adapter<TransactionViewAdapter.ViewHolder> {
    private List<? extends Transaction> mData = new LinkedList<>();
    private SelectionTracker<Long> mSelection;

    private List<String> mTempData = Arrays.asList(new String[]{"first", "second", "third"});

    public void setSelectionTracker(SelectionTracker<Long> tracker) {
        mSelection = tracker;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_transaction, parent, false);
        ViewHolder vh = new ViewHolder(view, this);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTransactionName.setText(mTempData.get(position));
//        Transaction trans = mData.get(position);
//
//        holder.txtTransactionName.setText(trans.getName());
//        holder.txtTransactionPrice.setText(Double.toString(trans.getAmount()));

        if (mSelection != null) {
            Log.e("Spender", "setting selection on " + position + " to " + mSelection.isSelected(new Long(position)));

            holder.bind(mSelection.isSelected(new Long(position)));
        } else {
            Log.e("Spender", "mSelection is null, not selecting this holder");
            holder.bind(false);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mTempData.size();
        //return mData.size();
    }


    public void submitData(@NonNull List<? extends Transaction> transactions) {
        mData = transactions;

        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTransactionName;
        private final TextView txtTransactionPrice;
        private final TextView txtTransactionDate;
        private final ImageView btnViewDetails;
        private final TransactionViewAdapter mAdapter;


        public ViewHolder(@NonNull View itemView, @NonNull TransactionViewAdapter adapter) {
            super(itemView);

            mAdapter = adapter;

            txtTransactionName = itemView.findViewById(R.id.txtTransName);
            txtTransactionPrice = itemView.findViewById(R.id.txtTransPrice);
            txtTransactionDate = itemView.findViewById(R.id.txtTransDate);
            btnViewDetails = itemView.findViewById(R.id.btnDetails);
            btnViewDetails.setOnClickListener(i -> Log.w("Spender", "view details here")); // todo
        }

        public void bind(boolean isSelected) {
            itemView.setActivated(isSelected);
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Nullable
                @Override
                public Long getSelectionKey() {
                    return getItemId();
                }
            };
        }

    }

    public static class TransactionItemDetailsLookup extends ItemDetailsLookup<Long> {
        private RecyclerView mRecyclerView;

        public TransactionItemDetailsLookup(@NonNull RecyclerView view) {
            mRecyclerView = view;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
            View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            ViewHolder vh = (ViewHolder)mRecyclerView.getChildViewHolder(child);

            return vh.getItemDetails();
        }
    }

    public static class TransactionKeyProvider extends ItemKeyProvider<Long> {
        private RecyclerView mRecyclerView;

        public TransactionKeyProvider(@NonNull RecyclerView view) {
            super(SCOPE_CACHED);

            mRecyclerView = view;
        }

        @Nullable
        @Override
        public Long getKey(int position) {
            return mRecyclerView.getAdapter().getItemId(position);
        }

        @Override
        public int getPosition(@NonNull Long key) {
            RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForItemId(key);

            return vh != null ? vh.getLayoutPosition() : RecyclerView.NO_POSITION;
        }
    }
}
