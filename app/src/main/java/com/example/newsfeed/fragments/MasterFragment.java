package com.example.newsfeed.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsfeed.R;
import com.example.newsfeed.adapters.NewsFeedAdapter;
import com.example.newsfeed.adapters.NewsItemClickListener;
import com.example.newsfeed.adapters.PinnedAdapter;
import com.example.newsfeed.data.models.News;
import com.example.newsfeed.fragments.base.BaseFragment;
import com.example.newsfeed.viewmodels.MasterViewModel;

public class MasterFragment extends BaseFragment implements NewsItemClickListener {

    private RecyclerView newsFeedRV, pinnedRv;
    private TextView pinnedLabelTv;
    private NewsFeedAdapter newsFeedAdapter;
    private PinnedAdapter pinnedAdapter;
    private MasterViewModel model;

    public static MasterFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MasterFragment fragment = new MasterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.layout_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.linear:
                newsFeedRV.setLayoutManager(new GridLayoutManager(getContext(), 1));
                newsFeedAdapter.notifyItemRangeChanged(0, newsFeedAdapter.getItemCount());
                break;
            case R.id.grid:
                newsFeedRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
                newsFeedAdapter.notifyItemRangeChanged(0, newsFeedAdapter.getItemCount());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_master, container, false);
    }

    @Override
    protected void setupViews() {
        newsFeedAdapter = new NewsFeedAdapter();
        newsFeedAdapter.setNewsItemClickListener(this);
        newsFeedRV.setLayoutManager(new GridLayoutManager(getContext(), 2));
        pinnedAdapter = new PinnedAdapter();
        pinnedAdapter.setNewsItemClickListener(this);
        pinnedRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void findViews(View view) {
        newsFeedRV = view.findViewById(R.id.news_feed_rv);
        pinnedRv = view.findViewById(R.id.pinned_rv);
        pinnedLabelTv = view.findViewById(R.id.pinned_label_tv);
    }

    @Override
    protected void bindModel() {
        model = ViewModelProviders.of(this).get(MasterViewModel.class);
        model.getNewsFeed().observe(this, news -> {
            newsFeedAdapter.submitList(news);
            newsFeedRV.setAdapter(newsFeedAdapter);
        });
        model.getPinnedNews().observe(this, pinnedNews -> {
            if (pinnedNews.size() != 0) {
                pinnedLabelTv.setVisibility(View.VISIBLE);
                pinnedRv.setVisibility(View.VISIBLE);
                pinnedAdapter.submitList(pinnedNews);
                pinnedRv.setAdapter(pinnedAdapter);
            } else {
                pinnedLabelTv.setVisibility(View.GONE);
                pinnedRv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void setupClicks() {
        //no clickable views
    }

    @Override
    public void onNewsItemClick(int position, News news, ImageView image) {
        changeListener.changeWithSharedElement(DetailFragment.newInstance(news.getId(), ViewCompat.getTransitionName(image)), image);
    }
}
