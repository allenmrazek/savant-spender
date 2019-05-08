package com.savantspender.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.savantspender.SavantSpender;
import com.savantspender.db.entity.Goal;
import com.savantspender.model.DataRepository;

import java.util.List;

public class GoalHeaderViewModel extends ViewModel {
    public final LiveData<List<? extends Goal>> goals;

    public GoalHeaderViewModel(DataRepository repository) {
        goals = Transformations.map(repository.goals(), l -> l);
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
            return (T) new GoalHeaderViewModel(mApplication.getRepository());
        }
    }
}
