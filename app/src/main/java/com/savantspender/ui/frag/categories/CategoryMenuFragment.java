package com.savantspender.ui.frag.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.R;
import com.savantspender.viewmodel.CategoryViewModel;

public class CategoryMenuFragment extends Fragment {
    private CategoryViewModel mViewModel;
    private TableLayout mTable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_categories, container, false);

        mViewModel = ViewModelProviders.of(getActivity(), new CategoryViewModel.Factory(getActivity().getApplication())).get(CategoryViewModel.class);

//        mTable = view.findViewById(R.id.tableCategories);
//
//        for (int r = 0; r < 30; ++r) {
//            TableRow row = new TableRow(getContext());
//
//            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
//
//            for (int c = 0; c < 4; ++c) {
//                View icon = inflater.inflate(R.layout.listitem_category, mTable, false);
//
//                row.addView(icon);
//            }
//
//            mTable.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            Log.e("Spender", "added row");
//        }


        /* Find Tablelayout defined in main.xml */
        TableLayout tl = (TableLayout) view.findViewById(R.id.tableCategories);

        for (int r = 0; r < 30; ++r) {
            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (int c = 0; c < 4; ++c) {
                /* Create a new row to be added. */
                View item = inflater.inflate(R.layout.listitem_category, tr, false);

                tr.addView(item);


//                /* Create a Button to be the row-content. */
//                Button b = new Button(getContext());
                //b.setText("Dynamic Button");
                //b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                /* Add Button to row. */
                //tr.addView(b);
            }
            /* Add row to TableLayout. */
//tr.setBackgroundResource(R.drawable.sf_gradient_03);
            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.availableTags().observe(getViewLifecycleOwner(), i -> {
            // todo
        });
    }
}
