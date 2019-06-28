package com.example.newsfeed.services;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.example.newsfeed.NewsFeedApp;
import com.example.newsfeed.R;
import com.example.newsfeed.activities.MainActivity;
import com.example.newsfeed.data.AppDatabase;
import com.example.newsfeed.data.Repository;
import com.example.newsfeed.data.models.News;
import com.example.newsfeed.eventbus.ReloadIfNeeded;
import com.example.newsfeed.network.NetworkManager;
import com.example.newsfeed.network.responses.SearchResponse;
import com.example.newsfeed.utils.AppExecutors;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFetchService extends Service {

    public static final String ACTION_FETCH = "fetch_data";
    private News newestNews;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.getInstance().setDatabase(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "news_feed_database").build());
        AppExecutors.getInstance().diskIO().execute(() -> {
            newestNews = Repository.getInstance().getNewestNews();
        });
        startForeground(8, createServiceNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(ACTION_FETCH)) {
                fetchData(1);
            }
        } else {
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Notification createServiceNotification() {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NewsFeedApp.FETCH_SERVICE_CHANNEL)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(getString(R.string.service_title))
                        .setContentText("")
                        .setPriority(NotificationManager.IMPORTANCE_DEFAULT);
        return notificationBuilder.build();
    }

    private void createNewItemNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NewsFeedApp.MAIN_CHANNEL)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(getString(R.string.fresh_news_title))
                        .setContentText(getString(R.string.feed_refresh_message))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(100, notificationBuilder.build());
    }

    private void freshNewsDetected() {
        if (!NewsFeedApp.isActivityVisible()) {
            createNewItemNotification();
        }
    }

    private void fetchData(int page) {
        NetworkManager.getApiService().getFeedData(page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    List<News> newsList = response.body().getResponse().getNews();
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        int count = Repository.getInstance().getNewsCount();
                        Repository.getInstance().addFetchedData(newsList);
                        if (count == 0) {
                            EventBus.getDefault().post(new ReloadIfNeeded());
                        }
                    });
                    boolean needToContinue = true;
                    if (newestNews != null) {
                        for (int i = 0; i < newsList.size(); i++) {

                            if (i == 0 && !newsList.get(i).getId().equals(newestNews.getId())) {
                                freshNewsDetected();
                            }
                            if (newsList.get(i).getId().equals(newestNews.getId())) {
                                needToContinue = false;
                                break;
                            }
                        }
                    } else {
                        needToContinue = false;
                    }
                    if (needToContinue) {
                        fetchData(page + 1);
                    } else {
                        stopSelf();
                    }
                } else {
                    stopSelf();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                stopSelf();
            }
        });
    }

    @Override
    public void onDestroy() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DataFetchService.class);
        intent.setAction(ACTION_FETCH);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent pendingIntent = PendingIntent.getForegroundService(this, 0, intent, 0);
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent);
        } else {
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 30000, pendingIntent);
        }
        super.onDestroy();
    }
}
