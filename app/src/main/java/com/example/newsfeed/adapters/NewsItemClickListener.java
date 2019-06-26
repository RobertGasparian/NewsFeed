package com.example.newsfeed.adapters;

import android.widget.ImageView;

import com.example.newsfeed.data.models.News;

public interface NewsItemClickListener {
    void onNewsItemClick(int position, News news, ImageView image);
}
