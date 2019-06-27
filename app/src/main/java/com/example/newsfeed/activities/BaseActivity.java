package com.example.newsfeed.activities;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.newsfeed.NewsFeedApp;
import com.example.newsfeed.R;
import com.example.newsfeed.fragments.base.BaseFragment;
import com.example.newsfeed.fragments.base.FragmentChangeListener;

public abstract class BaseActivity extends AppCompatActivity implements FragmentChangeListener {

    @Override
    public void change(BaseFragment fragment) {
        fragment.setChangeListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void changeWithSharedElement(BaseFragment fragment, ImageView imageView) {
        fragment.setChangeListener(this);
        getSupportFragmentManager().beginTransaction()
                .addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
                .replace(R.id.root_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NewsFeedApp.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NewsFeedApp.activityPaused();
    }
}
