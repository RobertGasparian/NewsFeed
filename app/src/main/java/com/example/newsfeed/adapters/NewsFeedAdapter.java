package com.example.newsfeed.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsfeed.R;
import com.example.newsfeed.adapters.viewholders.NewsGridHolder;
import com.example.newsfeed.adapters.viewholders.NewsHolder;
import com.example.newsfeed.adapters.viewholders.NewsLinearHolder;
import com.example.newsfeed.data.models.News;

public class NewsFeedAdapter extends PagedListAdapter<News, NewsHolder> {

    private static final int LINEAR_TYPE = 0;
    private static final int GRID_TYPE = 1;

    private NewsItemClickListener listener;

    private RecyclerView recyclerView;

    public NewsFeedAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setNewsItemClickListener(NewsItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == LINEAR_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_linear, parent, false);
            return new NewsLinearHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_grid, parent, false);
            return new NewsGridHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
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

    @Override
    public int getItemViewType(int position) {
        return ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount() == 1 ? LINEAR_TYPE: GRID_TYPE;
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
