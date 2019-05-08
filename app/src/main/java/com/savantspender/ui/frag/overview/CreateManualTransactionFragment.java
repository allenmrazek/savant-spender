package com.savantspender.ui.frag.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.savantspender.R;
import com.savantspender.viewmodel.MainViewModel;

public class CreateManualTransactionFragment extends DialogFragment {
    private MainViewModel mViewModel;
    private EditText mTransName;
    private EditText mTransAmount;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_manual_transaction, container, true);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mTransName = view.findViewById(R.id.txtNewTransName);
        mTransAmount = view.findViewById(R.id.txtManualTransAmt);

        view.findViewById(R.id.btnAddManualTrans).setOnClickListener(l -> onAccept());

        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        mViewModel.closingNewTransDlg().observe(getViewLifecycleOwner(), l -> dismissAllowingStateLoss());

        return view;
    }

    private void onAccept() {
        mViewModel.createManualTransaction(mTransName.getText().toString(), mTransAmount.getText().toString());
    }


    @Override
    public void onStart() {
        super.onStart();

        Window w = getDialog().getWindow();

        if (w != null)
            w.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
}
