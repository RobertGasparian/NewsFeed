package com.example.newsfeed.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsfeed.R;
import com.example.newsfeed.adapters.NewsFeedAdapter;
import com.example.newsfeed.fragments.base.BaseFragment;
import com.example.newsfeed.viewmodels.MasterViewModel;

public class MasterFragment extends BaseFragment {

    private RecyclerView newsFeedRV;
    private NewsFeedAdapter adapter;
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
        adapter = new NewsFeedAdapter();
        newsFeedRV.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected void findViews(View view) {
        newsFeedRV = view.findViewById(R.id.news_feed_rv);
    }

    @Override
    protected void bindModel() {
        model = ViewModelProviders.of(this).get(MasterViewModel.class);
        model.getNewsFeed().observe(this, news -> {
            adapter.submitList(news);
            newsFeedRV.setAdapter(adapter);
        });
    }

    @Override
    protected void setupClicks() {
        //no clickable views
    }
}
