package com.savantspender.ui.frag.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.savantspender.R;
import com.savantspender.ui.MainActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;

public class OverviewMenuFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_overview, container, false);

        view.findViewById(R.id.btnViewGoalList).setOnClickListener(l -> {
            ((MainActivity)getActivity()).TransitionTo(new GoalListFragment(), true);
        });

        view.findViewById(R.id.btnSummary).setOnClickListener(l -> {
            Toast.makeText(getContext(), "coming soon", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.btnCreateManual).setOnClickListener(l -> {
            CreateManualTransactionFragment mtf = new CreateManualTransactionFragment();

            mtf.show(getFragmentManager(), "dialog_createtrans");
        });

        TextView dateText = view.findViewById(R.id.txtOverviewDate);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");

        dateText.setText(sdf.format(Calendar.getInstance().getTime()));

        return view;
    }
}
