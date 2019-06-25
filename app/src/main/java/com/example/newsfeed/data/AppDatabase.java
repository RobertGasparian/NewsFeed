package com.example.newsfeed.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.newsfeed.data.models.News;

@Database(entities = {News.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewsDAO getNewsDAO();
}
