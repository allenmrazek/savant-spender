package com.savantspender.ui.frag.categories;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        TableLayout tl = view.findViewById(R.id.tableCategories);

        Point size = new Point();

        getActivity().getWindow().getWindowManager().getDefaultDisplay().getSize(size);

        float catWidth = getResources().getDimension(R.dimen.listitem_category_width);
        float catHeight = getResources().getDimension(R.dimen.listitem_category_height);


        long numCols = Math.round(Math.floor(size.x / catWidth));

        Log.e("Spender", "catWidth: " + catWidth + "," + catHeight + "; screen " + size.x + "," + size.y + "; calculated " + numCols);

        for (int r = 0; r < 30; ++r) {
            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for (long c = 0; c < numCols; ++c) {
                View item = inflater.inflate(R.layout.listitem_category, tr, false);

                item.setActivated(r % 6 == 0);
                tr.addView(item);
            }

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
