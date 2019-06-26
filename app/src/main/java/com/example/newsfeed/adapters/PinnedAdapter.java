package com.example.newsfeed.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.example.newsfeed.R;
import com.example.newsfeed.adapters.viewholders.NewsLinearHolder;
import com.example.newsfeed.data.models.News;

public class PinnedAdapter extends PagedListAdapter<News, NewsLinearHolder> {

    private NewsItemClickListener listener;

    public PinnedAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setNewsItemClickListener(NewsItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsLinearHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_grid, parent, false);
        return new NewsLinearHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsLinearHolder holder, int position) {
        News news = getItem(position);
        if (news != null) {
            holder.bind(news);
            if (listener != null) {
                holder.itemView.setOnClickListener(v -> {
                    listener.onNewsItemClick(position, news, holder.getImageForAnimation());
                });
            }
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
