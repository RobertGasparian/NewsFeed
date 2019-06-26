package com.example.newsfeed.fragments;

import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.newsfeed.R;
import com.example.newsfeed.fragments.base.BaseFragment;
import com.example.newsfeed.viewmodels.DetailViewModel;

public class DetailFragment extends BaseFragment {

    private static final String NEWS_ID_KEY = "news_id_key";
    private static final String TRANSITION_NAME_KEY = "transition_name_key";

    private ImageView thumbnailIv;

    private DetailViewModel model;

    public static DetailFragment newInstance(String newsId, String transitionName) {

        Bundle args = new Bundle();
        args.putString(NEWS_ID_KEY, newsId);
        args.putString(TRANSITION_NAME_KEY, transitionName);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void setupViews() {
        String transitionName = getArguments().getString(TRANSITION_NAME_KEY);
        thumbnailIv.setTransitionName(transitionName);
    }

    @Override
    protected void findViews(View view) {
        thumbnailIv = view.findViewById(R.id.thumbnail_iv);
    }

    @Override
    protected void bindModel() {
        model = ViewModelProviders.of(this).get(DetailViewModel.class);
        model.getDetailedNews(getArguments().getString(NEWS_ID_KEY)).observe(this, news -> {
            Glide.with(this).load(news.getFields().getThumbnail()).into(thumbnailIv);
        });
    }

    @Override
    protected void setupClicks() {

    }
}
