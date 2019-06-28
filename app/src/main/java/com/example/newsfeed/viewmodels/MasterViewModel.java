package com.example.newsfeed.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;

import com.example.newsfeed.data.Repository;
import com.example.newsfeed.data.models.News;

public class MasterViewModel extends AndroidViewModel {

    private LiveData<PagedList<News>> newsLD;
    private LiveData<PagedList<News>> pinedLD;

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

    //In Room there is no way to get observable entity count
    public int getNewsCount() {
        return Repository.getInstance().getNewsCount();
    }

    public void loadMore() {
        Repository.getInstance().loadMore((getNewsCount() / 10) + 1);
    }
}
