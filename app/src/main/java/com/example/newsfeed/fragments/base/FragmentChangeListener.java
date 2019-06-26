package com.example.newsfeed.fragments.base;

import android.widget.ImageView;

public interface FragmentChangeListener {
    void change(BaseFragment fragment);
    void changeWithSharedElement(BaseFragment fragment, ImageView imageView);
}
