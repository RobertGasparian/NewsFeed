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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(News...news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNews(List<News> news);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNews(News...news);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNews(List<News> news);

    @Delete
    void deleteNews(News...news);

    @Delete
    void deleteNews(List<News> news);

    //QUERIES

    @Query("SELECT * FROM news_feed_table ORDER BY web_publication_date DESC")
    DataSource.Factory<Integer, News> getNewsFeed();

    @Query("SELECT * FROM news_feed_table WHERE is_pinned = 1 ORDER BY web_publication_date DESC")
    DataSource.Factory<Integer, News> getPinnedNews();

    @Query("SELECT * FROM news_feed_table WHERE id = :id")
    LiveData<News> getNewsById(String id);

    @Query("SELECT * FROM news_feed_table ORDER BY web_publication_date ASC LIMIT 1")
    LiveData<News> getOldestNews();

    @Query("SELECT * FROM news_feed_table ORDER BY web_publication_date DESC LIMIT 1")
    News getNewestNews();

    @Query("SELECT COUNT(id) FROM news_feed_table")
    LiveData<Integer> getNewsCount();

    @Query("SELECT COUNT(id) FROM news_feed_table")
    Integer getNewsCountTest();
}
