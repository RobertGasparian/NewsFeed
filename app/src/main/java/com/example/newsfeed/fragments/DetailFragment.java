package com.example.newsfeed.fragments;

import android.os.Bundle;
import android.view.View;

import com.example.newsfeed.fragments.base.BaseFragment;

public class DetailFragment extends BaseFragment {

    public static DetailFragment newInstance() {

        Bundle args = new Bundle();

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupViews() {

    }

    @Override
    protected void findViews(View view) {

    }

    @Override
    protected void bindModel() {

    }

    @Override
    protected void setupClicks() {

    }
}
