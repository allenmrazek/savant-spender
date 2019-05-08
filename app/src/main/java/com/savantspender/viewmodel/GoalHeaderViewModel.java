package com.savantspender.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.savantspender.SavantSpender;
import com.savantspender.model.DataRepository;

public class GoalHeaderViewModel extends ViewModel {

    public GoalHeaderViewModel(DataRepository repository) {

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
