package com.example.newsfeed;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.room.Room;

import com.example.newsfeed.data.AppDatabase;
import com.example.newsfeed.data.Repository;
import com.example.newsfeed.services.DataFetchService;

public class NewsFeedApp extends Application {

    public static final String MAIN_CHANNEL = "main_channel";
    public static final String MAIN_CHANNEL_NAME = "NewsFeed Notifications";
    public static final String FETCH_SERVICE_CHANNEL = "fetch_service_channel";
    public static final String FETCH_SERVICE_CHANNEL_NAME = "NewsFeed Data Fetching Service";
    public static final String MAIN_CHANNEL_DESCRIPTION = "Notifications from NewsFeed Application.";
    public static final String FETCH_SERVICE_CHANNEL_DESCRIPTION = "Background service for fetching News";



    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        Repository.getInstance().setDatabase(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "news_feed_database").build());
        Intent intent = new Intent(this, DataFetchService.class);
        intent.setAction(DataFetchService.ACTION_FETCH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    public void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int mainImp = NotificationManager.IMPORTANCE_DEFAULT;
            int serviceImp = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mainChannel = new NotificationChannel(MAIN_CHANNEL, MAIN_CHANNEL_NAME, mainImp);
            NotificationChannel serviceChannel = new NotificationChannel(FETCH_SERVICE_CHANNEL, FETCH_SERVICE_CHANNEL_NAME, serviceImp);
            mainChannel.setDescription(MAIN_CHANNEL_DESCRIPTION);
            serviceChannel.setDescription(FETCH_SERVICE_CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mainChannel);
                notificationManager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
