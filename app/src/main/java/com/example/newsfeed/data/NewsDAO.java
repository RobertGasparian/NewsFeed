package com.example.newsfeed.data;


import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.newsfeed.data.models.Fields;
import com.example.newsfeed.data.models.News;

import java.util.List;

@Dao
public interface NewsDAO {

    //INSERT, UPDATE, DELETE
    //News

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertNews(News...news);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertNews(List<News> news);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public void updateNews(News...news);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public void updateNews(List<News> news);

    @Delete
    public void deleteNews(News...news);

    @Delete
    public void deleteNews(List<News> news);

    //QUERIES

    @Query("SELECT * FROM news_feed_table WHERE is_pinned = 0 ORDER BY web_publication_date DESC")
    public DataSource.Factory<Integer, News> getNewsFeed();

    @Query("SELECT * FROM news_feed_table WHERE is_pinned = 1 ORDER BY web_publication_date DESC")
    public DataSource.Factory<Integer, News> getPinnedNews();

    @Query("SELECT * FROM news_feed_table WHERE id = :id")
    public LiveData<News> getNewsById(String id);
}
