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
    private LiveData<PagedList<News>> pinedLD;
    private LiveData<News> oldestLD;
    private LiveData<Integer> newsCountLD;

    public MasterViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<PagedList<News>> getNewsFeed() {
        newsLD = Repository.getInstance().getNewsFeed();
        return newsLD;
    }

    public LiveData<PagedList<News>> getPinnedNews() {
        pinedLD = Repository.getInstance().getPinnedNews();
        return pinedLD;
    }

    public LiveData<News> getOldestNews() {
        oldestLD = Repository.getInstance().getOldestNews();
        return oldestLD;
    }

    public LiveData<Integer> getNewsCount() {
        newsCountLD = Repository.getInstance().getNewsCount();
        return newsCountLD;
    }

    public void loadMore(int position) {
        Repository.getInstance().loadMore(position);
    }
}
