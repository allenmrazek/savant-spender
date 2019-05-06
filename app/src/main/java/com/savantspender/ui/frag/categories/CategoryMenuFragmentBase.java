package com.savantspender.ui.frag.categories;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.savantspender.R;
import com.savantspender.db.entity.Tag;
import com.savantspender.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class CategoryMenuFragmentBase extends Fragment {
    protected FloatingActionButton mFloatingAccept;

    protected CategoryViewModel mViewModel;
    protected TableLayout mTable;
    protected List<? extends Tag> mTags = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_categories, container, false);

        mViewModel = ViewModelProviders.of(getActivity(), new CategoryViewModel.Factory(getActivity().getApplication())).get(CategoryViewModel.class);
        mTable = view.findViewById(R.id.tableCategories);

        mFloatingAccept = view.findViewById(R.id.btnFloatingAccept);
        mFloatingAccept.hide();

        return view;
    }


    protected void buildTable(List<? extends Tag> tags) {
        mTable.removeAllViews();

        mTags = tags;

        Log.w("Spender", "rebuilding cat table");

        Point size = new Point();

        getActivity().getWindow().getWindowManager().getDefaultDisplay().getSize(size);

        float catWidth = getResources().getDimension(R.dimen.listitem_category_width);

        long numCols = Math.round(Math.floor(size.x / catWidth));
        long curItem = 0;
        TableRow tr = new TableRow(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        tr.setLayoutParams(params);

        ArrayList<TableRow> rows = new ArrayList<>();

        for (Tag tag : tags) {
            if (curItem % numCols == 0) {
                tr = new TableRow(getContext());
                tr.setLayoutParams(params);

                rows.add(tr);
            }

            View item = getLayoutInflater().inflate(R.layout.listitem_category, tr, false);

            TextView tv = item.findViewById(R.id.txtCatName);
            tv.setText(tag.getName());

            onItemCreated(item, tag);

            tr.addView(item);
            ++curItem;

        }

        for (TableRow row : rows)
            mTable.addView(row);

        // todo: add tag button?
    }

    protected abstract void onItemCreated(View item, Tag which);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.availableTags().observe(getViewLifecycleOwner(), tags -> {
            buildTable(tags);
        });
    }
}
