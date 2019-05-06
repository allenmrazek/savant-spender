//package com.savantspender.ui.frag.categories;
//
//import android.graphics.Point;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProviders;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.savantspender.R;
//import com.savantspender.db.entity.Tag;
//import com.savantspender.db.entity.Transaction;
//import com.savantspender.ui.frag.transactions.SortedTransactionsFragment;
//import com.savantspender.viewmodel.CategoryViewModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CategoryMenuFragment extends Fragment {
//    private enum MODE { CREATE_VIEW, CHOOSE };
//
//    private FloatingActionButton mFloatingAccept;
//
//    private CategoryViewModel mViewModel;
//    private TableLayout mTable;
//    private List<Transaction> mTransactions;
//    private List<? extends Tag> mTags = new ArrayList<>();
//    private int mSelectedCount = 0;
//    private MODE mMode = MODE.CREATE_VIEW;
//
//    public CategoryMenuFragment() {}
//
//    public CategoryMenuFragment(@NonNull List<Transaction> transactions) {
//        mTransactions = transactions;
//        mMode = MODE.CHOOSE;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//
//        Log.w("Spender", "Category menu fragment detaching");
//
//        if (mTransactions != null)
//            mTransactions.clear();
//    }
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_menu_categories, container, false);
//
//        mViewModel = ViewModelProviders.of(getActivity(), new CategoryViewModel.Factory(getActivity().getApplication())).get(CategoryViewModel.class);
//        mTable = view.findViewById(R.id.tableCategories);
//
//        if (mTransactions != null && mTransactions.size() > 0)
//            mViewModel.stashTaglessTransactions(mTransactions);
//
//        mFloatingAccept = view.findViewById(R.id.btnFloatingAccept);
//        mFloatingAccept.hide();
//
//        mFloatingAccept.setOnClickListener(l -> onFloatingAcceptClick());
//
//
//        return view;
//    }
//
//    public void onFloatingAcceptClick() {
//        if (mMode == MODE.CHOOSE) {
//            Fragment invoker = getTargetFragment();
//
//            if (invoker != null && invoker instanceof SortedTransactionsFragment) {
//                SortedTransactionsFragment sorter = (SortedTransactionsFragment)invoker;
//                sorter.transactionsWereSorted();
//
//                getFragmentManager().popBackStack();
//            }
//        } else {
//            Log.e("Spender", "catalog fragment in wrong mode");
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        updateFloatingAcceptState();
//    }
//
//
//    private void updateFloatingAcceptState() {
//        if (mMode == MODE.CHOOSE && mSelectedCount > 0) {
//            mFloatingAccept.show();
//        } else mFloatingAccept.hide();
//    }
//
//    private void buildTable(List<? extends Tag> tags) {
//        mTable.removeAllViews();
//
//        mTags = tags;
//        mSelectedCount = 0;
//
//        Log.w("Spender", "rebuilding cat table");
//
//        Point size = new Point();
//
//        getActivity().getWindow().getWindowManager().getDefaultDisplay().getSize(size);
//
//        float catWidth = getResources().getDimension(R.dimen.listitem_category_width);
//
//        long numCols = Math.round(Math.floor(size.x / catWidth));
//        long curItem = 0;
//        TableRow tr = new TableRow(getContext());
//        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
//
//        tr.setLayoutParams(params);
//
//        ArrayList<TableRow> rows = new ArrayList<>();
//
//        for (Tag tag : tags) {
//            if (curItem % numCols == 0) {
//                tr = new TableRow(getContext());
//                tr.setLayoutParams(params);
//
//                rows.add(tr);
//            }
//
//            View item = getLayoutInflater().inflate(R.layout.listitem_category, tr, false);
//
//            TextView tv = item.findViewById(R.id.txtCatName);
//            tv.setText(tag.getName());
//
//            ImageView iv = item.findViewById(R.id.imgCatBackground);
//
//            iv.setActivated(tag.isSelected());
//
//            item.setOnClickListener(l -> {
//                Log.e("Spender", "Clicked tag " + tag.getName() + ", " + tag.getId());
//
//                tag.setSelected(tag.isSelected());
//                iv.setActivated(tag.isSelected());
//
//                mSelectedCount += tag.isSelected() ? 1 : -1;
//                updateFloatingAcceptState();
//            });
//
//            tr.addView(item);
//            ++curItem;
//
//            if (tag.isSelected()) ++mSelectedCount;
//        }
//
//        for (TableRow row : rows)
//            mTable.addView(row);
//
//        // todo: add tag button?
//    }
//
//
//    private void addNewTagButton() {
//        // todo
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        mViewModel.availableTags().observe(getViewLifecycleOwner(), tags -> {
//            buildTable(tags);
//        });
//    }
//}
