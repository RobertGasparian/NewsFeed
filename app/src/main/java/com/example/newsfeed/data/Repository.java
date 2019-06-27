package com.example.newsfeed.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.newsfeed.adapters.NewsFeedAdapter;
import com.example.newsfeed.adapters.rvutils.LayoutMode;
import com.example.newsfeed.data.models.Fields;
import com.example.newsfeed.data.models.News;
import com.example.newsfeed.eventbus.ErrorEvent;
import com.example.newsfeed.network.ApiService;
import com.example.newsfeed.network.NetworkManager;
import com.example.newsfeed.network.responses.SearchResponse;
import com.example.newsfeed.utils.AppExecutors;

import org.greenrobot.eventbus.EventBus;

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

    public LiveData<Integer> getNewsCount() {
        return database.getNewsDAO().getNewsCount();
    }

    public void loadMore(int position) {
        Log.d("page_checker", "" + ((position / 10) + 1));
        apiService.getFeedData((position / 10) + 1).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    List<News> newsList = response.body().getResponse().getNews();

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        Integer old = database.getNewsDAO().getNewsCountTest();
                        Log.d("old_new_checker", "old - " + old);
                        for (News news :
                                newsList) {
                            Log.d("id_checker", news.getId());
                        }

                        database.getNewsDAO().insertNews(newsList);
                        Integer new1 = database.getNewsDAO().getNewsCountTest();
                        Log.d("old_new_checker", "new - " + new1);
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
        AppExecutors.getInstance().diskIO().execute(() -> database.getNewsDAO().insertNews(news));
    }
}
