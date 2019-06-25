package com.example.newsfeed.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.newsfeed.data.Repository;
import com.example.newsfeed.data.models.News;

public class MasterViewModel extends AndroidViewModel {

    private LiveData<PagedList<News>> newsLD;

    public MasterViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<PagedList<News>> getNewsFeed() {
        newsLD = Repository.getInstance().getNewsFeed();
        return newsLD;
    }
}
