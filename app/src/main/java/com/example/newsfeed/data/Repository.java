package com.example.newsfeed.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.newsfeed.adapters.rvutils.LayoutMode;
import com.example.newsfeed.data.models.News;
import com.example.newsfeed.eventbus.ChangeCountEvent;
import com.example.newsfeed.eventbus.ErrorEvent;
import com.example.newsfeed.network.ApiService;
import com.example.newsfeed.network.NetworkManager;
import com.example.newsfeed.network.responses.SearchResponse;
import com.example.newsfeed.utils.AppExecutors;

import org.greenrobot.eventbus.EventBus;

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
        return new LivePagedListBuilder<>(database.getNewsDAO().getNewsFeed(), 10)
                .setFetchExecutor(AppExecutors.getInstance().diskIO())
                .build();
    }

    public LiveData<PagedList<News>> getPinnedNews() {
        return new LivePagedListBuilder<>(database.getNewsDAO().getPinnedNews(), 10)
                .setFetchExecutor(AppExecutors.getInstance().diskIO())
                .build();
    }

    public void populateNewsFeed() {
        apiService.getFeedData(1).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    List<News> newsList = response.body().getResponse().getNews();
                    AppExecutors.getInstance().diskIO().execute(() -> database.getNewsDAO().insertNews(newsList));
                } else {
                    EventBus.getDefault().post(new ErrorEvent("Something went wrong with internet request."));
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                EventBus.getDefault().post(new ErrorEvent(t.getMessage()));
            }
        });
    }

    public LiveData<News> getNewsById(String id) {
        return database.getNewsDAO().getNewsById(id);
    }

    public void updateNews(News news) {
        AppExecutors.getInstance().diskIO().execute(() -> database.getNewsDAO().updateNews(news));
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

    public News getNewestNews() {
        return database.getNewsDAO().getNewestNews();
    }

    public LiveData<News> getOldestNews() {
        return database.getNewsDAO().getOldestNews();
    }

    public int getNewsCount() {
        return database.getNewsDAO().getNewsCount();
    }

    public void loadMore(int page) {
        apiService.getFeedData(page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    List<News> newsList = response.body().getResponse().getNews();

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        int oldCount = database.getNewsDAO().getNewsCount();
                        database.getNewsDAO().insertNews(newsList);
                        int newCount = database.getNewsDAO().getNewsCount();
                        if (oldCount == newCount) {
                            int nextPage = page + 1;
                            loadMore(nextPage);
                        } else {
                            EventBus.getDefault().post(new ChangeCountEvent(newCount));
                        }
                    });
                } else {
                    EventBus.getDefault().post(new ErrorEvent("Something went wrong with internet request."));
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                EventBus.getDefault().post(new ErrorEvent(t.getMessage()));
            }
        });
    }

    public void addFetchedData(List<News> news) {
        database.getNewsDAO().insertNews(news);
    }
}
