package com.savantspender.ui.frag.overview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.R;
import com.savantspender.db.entity.Tag;
import com.savantspender.viewmodel.GoalsViewModel;

import java.util.ArrayList;
import java.util.List;

public class CreateGoalFragment extends DialogFragment {
    private GoalsViewModel mViewModel;
    private TableLayout mTable;
    private EditText mGoalName;
    private EditText mAmount;

    private List<? extends Tag> mTags;
    private int mNumCols = 0;

    private void availableTagsChanged(List<? extends Tag> tags) {
        mTags = tags == null ? new ArrayList<>() : tags;

        buildTagTable();
    }

    private void buildTagTable() {
        mTable.removeAllViews();

        if (mTags == null || mTags.size() == 0 || mNumCols == 0)
            return;


        long curItem = 0;
        TableRow tr = new TableRow(getContext());
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        tr.setLayoutParams(params);

        ArrayList<TableRow> rows = new ArrayList<>();

        for (int i = 0; i < 20; ++i) { // temp to test layout


            for (Tag tag : mTags) {
                if (curItem % mNumCols == 0) {
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

        }
        for (TableRow row : rows)
            mTable.addView(row);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mViewModel.closingDialog();
    }


    private void onItemCreated(View view, Tag tag) {
        view.setOnClickListener(l -> {
            l.setActivated(!l.isActivated());
            tag.setSelected(l.isActivated());
        });
    }


    private void onAccept() {
        Log.e("Spender", "onAccept");

        // verify parameters
        mViewModel.createGoal(mGoalName.getText().toString(), mAmount.getText().toString());

    }


    private void finish() {
        mViewModel.closingDialog();
        dismissAllowingStateLoss();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_creategoal, container, true);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mTable = view.findViewById(R.id.NewGoalTagLayout);
        mGoalName = view.findViewById(R.id.txtNewGoalName);
        mAmount = view.findViewById(R.id.txtNewGoalAmount);

        view.findViewById(R.id.btnAcceptAddGoal).setOnClickListener(l -> onAccept());

        mViewModel = ViewModelProviders.of(getActivity(), new GoalsViewModel.Factory(getActivity().getApplication())).get(GoalsViewModel.class);
        mViewModel.availableTags().observe(getViewLifecycleOwner(), t -> availableTagsChanged(t));
        mViewModel.goalWasCreated().observe(getViewLifecycleOwner(), t -> finish());

        view.post(() -> {
            int width  = view.getMeasuredWidth();
            float catWidth = getResources().getDimension(R.dimen.listitem_category_width);

            mNumCols = Math.round((float)Math.floor(width / catWidth));
            buildTagTable();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window w = getDialog().getWindow();

        if (w != null)
            w.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
}
