package com.savantspender.ui.frag.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.R;
import com.savantspender.SavantSpender;
import com.savantspender.db.entity.Tag;
import com.savantspender.db.entity.Transaction;
import com.savantspender.viewmodel.CategorizerViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryMenuFragmentCategorize extends CategoryMenuFragmentBase {
    private int mSelectedCount = 0;
    private List<? extends Transaction> mContainer;
    private CategorizerViewModel mCategorizerViewModel;


    public static CategoryMenuFragmentCategorize newInstance(@NonNull List<? extends Transaction> transactions) {
        CategoryMenuFragmentCategorize frag = new CategoryMenuFragmentCategorize();

        frag.mContainer = transactions;

        return frag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result = super.onCreateView(inflater, container, savedInstanceState);

        mCategorizerViewModel = ViewModelProviders.of(this, new CategorizerViewModel.Factory(getActivity().getApplication())).get(CategorizerViewModel.class);

        if (mContainer != null)
            mCategorizerViewModel.setTransactions(mContainer);

        mCategorizerViewModel.needTags().observe(getViewLifecycleOwner(), l -> onNeedTags(l));
        mCategorizerViewModel.finished().observe(getViewLifecycleOwner(), l -> {
            if (l.isHandled()) return;

            l.setHandled();
            ((SavantSpender)getActivity().getApplication()).dispatchOneTimeGoalUpdate();
            getActivity().getSupportFragmentManager().popBackStack();
        });

        mFloatingAccept.setOnClickListener(l -> complete());
        return result;
    }

    private void onNeedTags(List<? extends Transaction> transactions) {
        mContainer = transactions;
        buildTable(mTags);
    }

    private void complete() {
        List<Tag> tags = new ArrayList<>();

        for (Tag tag : mTags)
            if (tag.isSelected())
                tags.add(tag);

        mCategorizerViewModel.categorize(tags, mContainer);
    }


    private void updateCounter() {
        mSelectedCount = 0;

        for (Tag tag : mTags)
            if (tag.isSelected())
                ++mSelectedCount;
    }

    @Override
    protected void buildTable(List<? extends Tag> tags) {
        super.buildTable(tags);
        updateCounter();
        updateFloatingAcceptState();
    }

    @Override
    protected void onItemCreated(View item, Tag which) {
        item.setOnClickListener(l -> {
            ConstraintLayout iv = item.findViewById(R.id.cat_icon_bkg);


            which.setSelected(!which.isSelected());
            iv.setActivated(which.isSelected());

            updateCounter();
            updateFloatingAcceptState();
        });

        item.setActivated(which.isSelected());
    }

    private void updateFloatingAcceptState() {
        if (mSelectedCount > 0) {
            mFloatingAccept.show();
        } else mFloatingAccept.hide();
    }
}
