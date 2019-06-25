package com.example.newsfeed.data;



import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.newsfeed.data.models.News;
import com.example.newsfeed.network.ApiService;
import com.example.newsfeed.network.NetworkManager;

public class Repository {

    private ApiService apiService;

    private static Repository instance;

    private AppDatabase database;

    public static synchronized Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    private Repository() {
        apiService = NetworkManager.getApiService();
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    public LiveData<PagedList<News>> getNewsFeed() {
        return new LivePagedListBuilder<>(database.getNewsDAO().getNewsFeed(), 10).build();
    }

}
