package com.example.newsfeed;

import android.app.Application;

import androidx.room.Room;

import com.example.newsfeed.data.AppDatabase;
import com.example.newsfeed.data.Repository;

public class NewsFeedApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Repository.getInstance().setDatabase(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "news_feed_database").build());
    }
}
