package com.example.newsfeed.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.newsfeed.data.models.News;
import com.example.newsfeed.data.typeconverters.FieldsTypeConverter;

@Database(entities = {News.class}, version = 1)
@TypeConverters({FieldsTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewsDAO getNewsDAO();
}
