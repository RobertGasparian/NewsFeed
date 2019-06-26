package com.example.newsfeed.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsfeed.R;
import com.example.newsfeed.adapters.NewsFeedAdapter;
import com.example.newsfeed.adapters.PinnedAdapter;
import com.example.newsfeed.fragments.base.BaseFragment;
import com.example.newsfeed.viewmodels.MasterViewModel;

public class MasterFragment extends BaseFragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_master, container, false);
    }

    @Override
    protected void setupViews() {
        newsFeedAdapter = new NewsFeedAdapter();
        newsFeedRV.setLayoutManager(new LinearLayoutManager(getContext()));
        pinnedAdapter = new PinnedAdapter();
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
}
