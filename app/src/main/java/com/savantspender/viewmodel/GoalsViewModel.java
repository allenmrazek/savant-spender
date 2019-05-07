package com.savantspender.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.savantspender.SavantSpender;
import com.savantspender.db.AppDatabase;
import com.savantspender.db.entity.Tag;
import com.savantspender.model.DataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class GoalsViewModel extends ViewModel {
    private final LiveData<List<? extends Tag>> mTags;

    private final DataRepository mRepository;
    private final AppDatabase mDatabase;
    private final Executor mDiskIO;

    public GoalsViewModel(DataRepository repository, AppDatabase db, Executor diskIO) {
        mRepository = repository;
        mDatabase = db;
        mDiskIO = diskIO;

        mTags = Transformations.map(mDatabase.tagDao().getTags(), l -> l);
    }


    public LiveData<List<? extends Tag>> availableTags() {
        return mTags;
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
