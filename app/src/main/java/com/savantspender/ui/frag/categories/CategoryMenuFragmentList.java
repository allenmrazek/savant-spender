package com.savantspender.ui.frag.categories;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.savantspender.db.entity.Transaction;
import com.savantspender.ui.frag.transactions.SortedTransactionsFragment;
import com.savantspender.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryMenuFragmentList extends CategoryMenuFragmentBase {


    @Override
    protected void onItemCreated(View item, Tag which) {

    }
}
