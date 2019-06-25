package com.example.newsfeed.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.newsfeed.R;
import com.example.newsfeed.adapters.viewholders.NewsHolder;
import com.example.newsfeed.data.models.News;

public class NewsFeedAdapter extends PagedListAdapter<News, NewsHolder> {

    public NewsFeedAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_linear, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News news = getItem(position);
        if (news != null) {
            holder.bind(news);
        } else {
            //placeholder
        }
    }

    private static DiffUtil.ItemCallback<News> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<News>() {

                @Override
                public boolean areItemsTheSame(News oldNews, News newNews) {
                    return oldNews.getId() == newNews.getId();
                }

                @Override
                public boolean areContentsTheSame(News oldNews,
                                                  News newNews) {
                    return oldNews.equals(newNews);
                }
            };
}
