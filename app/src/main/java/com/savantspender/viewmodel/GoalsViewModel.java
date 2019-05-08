package com.savantspender.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.savantspender.Event;
import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.Goal;
import com.savantspender.db.entity.GoalEntity;
import com.savantspender.db.entity.GoalTagsEntity;
import com.savantspender.db.entity.Tag;
import com.savantspender.model.DataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class GoalsViewModel extends ViewModel {
    private final LiveData<List<? extends Tag>> mTags;
    private final MutableLiveData<Event<Void>> mDialogClosed = new MutableLiveData<>();
    private final MutableLiveData<Event<Void>> mGoalCreated = new MutableLiveData<>();
    private final MutableLiveData<Event<String>> mToast = new MutableLiveData<>();
    private final LiveData<List<? extends Goal>> mGoals;


    private final DataRepository mRepository;
    private final AppDatabase mDatabase;
    private final Executor mDiskIO;

    public GoalsViewModel(DataRepository repository, AppDatabase db, Executor diskIO) {
        mRepository = repository;
        mDatabase = db;
        mDiskIO = diskIO;

        mTags = Transformations.map(mDatabase.tagDao().getTags(), l -> l);
        mGoals = Transformations.map(mDatabase.goalDao().getAll(), l -> l);
    }



    public LiveData<List<? extends Tag>> availableTags() {
        return mTags;
    }
    public LiveData<Event<Void>> dialogClosed() {
        return mDialogClosed;
    }
    public LiveData<Event<String>> toastMessage() {
        return mToast;
    }
    public LiveData<List<? extends Goal>> goals() {
        return mGoals;
    }



    public void closingDialog() {
        mDialogClosed.postValue(new Event<>(null));
    }

    public LiveData<Event<Void>> goalWasCreated() {
        return mGoalCreated;
    }


    private void makeToast(String text) {
        mToast.postValue(new Event<>(text));
    }

    public void createGoal(@NonNull String name, @NonNull String amount, List<? extends Tag> selectedTags) {

        // validate parameters
        if (name == null || name.length() == 0)
        {
            makeToast("invalid name");
            return;
        }

        if (amount == null || amount.length() == 0) {
            makeToast("invalid amount");
            return;
        }

        double numericAmount = 0.0;

        try {
            numericAmount = Double.parseDouble(amount);
        } catch (NumberFormatException ne) {
            makeToast("enter a numeric value");
            return;
        }

        if (numericAmount <= 0.0) {
            makeToast("enter a positive value");
            return;
        }

        if (selectedTags.size() == 0)
        {
            makeToast("select at least one tag");
            return;
        }

        final double amt = numericAmount;

        mDiskIO.execute(() -> {
            if (mDatabase.goalDao().exists(name)) {
                makeToast("\"" + name + "\" already exists!");
                return;
            }

            try {
                mDatabase.beginTransaction();

                mDatabase.goalDao().insert(new GoalEntity(name, amt));

                int id = mDatabase.goalDao().getByName(name);
                for (Tag t : selectedTags)
                    mDatabase.goalTagDao().insert(new GoalTagsEntity(id, t.getId()));

                mDatabase.setTransactionSuccessful();
                makeToast("\"" + name + "\" created!");
                mGoalCreated.postValue(new Event<>(null));

            } catch (Exception e) {
                Log.e("Spender", "failed to create goal: " + e.getMessage());
            }

            mDatabase.endTransaction();
        });
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final SavantSpender mApplication;

        public Factory(@NonNull Application application) {
            mApplication = (SavantSpender)application;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new GoalsViewModel(mApplication.getRepository(), mApplication.getDatabase(), mApplication.getExecutors().diskIO());
        }
    }
}
