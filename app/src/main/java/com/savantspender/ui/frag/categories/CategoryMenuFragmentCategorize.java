package com.savantspender.ui.frag.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.R;
import com.savantspender.db.entity.Tag;
import com.savantspender.db.entity.Transaction;
import com.savantspender.viewmodel.CategorizerViewModel;
import com.savantspender.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryMenuFragmentCategorize extends CategoryMenuFragmentBase {
    private int mSelectedCount = 0;
    private List<? extends Transaction> mContainer;
    private CategorizerViewModel mCategorizerViewModel;

    public CategoryMenuFragmentCategorize(@NonNull List<? extends Transaction> transactions) {
        mContainer = transactions;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = super.onCreateView(inflater, container, savedInstanceState);

        mCategorizerViewModel = ViewModelProviders.of(this, new CategorizerViewModel.Factory(getActivity().getApplication())).get(CategorizerViewModel.class);

        if (mContainer != null)
            mCategorizerViewModel.setTransactions(mContainer);

        mCategorizerViewModel.needTags().observe(getViewLifecycleOwner(), l -> onNeedTags(l));

        mFloatingAccept.setOnClickListener(l -> complete());
        return result;
    }

    private void onNeedTags(List<? extends Transaction> transactions) {
        mContainer = transactions;
    }

    private void complete() {
        List<Tag> tags = new ArrayList<>();

        for (Tag tag : mTags)
            if (tag.isSelected())
                tags.add(tag);

        mCategorizerViewModel.categorize(tags, mContainer);
        getActivity().getSupportFragmentManager().popBackStack();
    }


    @Override
    protected void onItemCreated(View item, Tag which) {
        item.setOnClickListener(l -> {
            Log.e("Spender", "Clicked tag " + which.getName() + ", " + which.getId());

            ImageView iv = item.findViewById(R.id.imgCatBackground);


            which.setSelected(!which.isSelected());
            iv.setActivated(which.isSelected());

            mSelectedCount += which.isSelected() ? 1 : -1;
            updateFloatingAcceptState();
        });
    }

    private void updateFloatingAcceptState() {
        if (mSelectedCount > 0) {
            mFloatingAccept.show();
        } else mFloatingAccept.hide();
    }
}
