package com.example.newsfeed.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.newsfeed.adapters.NewsFeedAdapter;
import com.example.newsfeed.adapters.rvutils.LayoutMode;
import com.example.newsfeed.data.models.Fields;
import com.example.newsfeed.data.models.News;
import com.example.newsfeed.network.ApiService;
import com.example.newsfeed.network.NetworkManager;
import com.example.newsfeed.network.responses.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    static private final String LAYOUT_MODE_KEY = "layout_mode_key";

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

    public LiveData<PagedList<News>> getPinnedNews() {
        return new LivePagedListBuilder<>(database.getNewsDAO().getPinnedNews(), 10).build();
    }

    public void populateNewsFeed() {
        apiService.getFeedData(1).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    List<News> newsList = response.body().getResponse().getNews();
                    new Thread() {
                        @Override
                        public void run() {
                            database.getNewsDAO().insertNews(newsList);
                        }
                    }.start();
                } else {
                    //todo handle error
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                //todo handle failure
            }
        });
    }

    public LiveData<News> getNewsById(String id) {
        return database.getNewsDAO().getNewsById(id);
    }

    public String getLayoutMode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(LAYOUT_MODE_KEY, LayoutMode.LINEAR.name());
    }

    public void setLayoutMode(Context context, LayoutMode mode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LAYOUT_MODE_KEY, mode.name());
        editor.apply();
    }
}
