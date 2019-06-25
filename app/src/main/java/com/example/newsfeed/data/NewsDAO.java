package com.example.newsfeed.data;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newsfeed.data.models.News;

import java.util.List;

@Dao
public interface NewsDAO {

    //INSERT, UPDATE, DELETE

    @Insert
    public void insertNews(News...news);

    @Insert
    public void insertNews(List<News> news);

    @Update
    public void updateNews(News...news);

    @Update
    public void updateNews(List<News> news);

    @Delete
    public void deleteNews(News...news);

    @Delete
    public void deleteNews(List<News> news);

    //QUERIES

    @Query("SELECT * FROM news_feed")
    public DataSource.Factory<Integer, News> getNewsFeed();
}
