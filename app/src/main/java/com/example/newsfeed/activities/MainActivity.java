package com.example.newsfeed.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.newsfeed.R;
import com.example.newsfeed.fragments.MasterFragment;
import com.example.newsfeed.fragments.base.BaseFragment;
import com.example.newsfeed.fragments.base.FragmentChangeListener;

public class MainActivity extends AppCompatActivity implements FragmentChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupMasterFragment();
    }

    private void setupMasterFragment() {
        MasterFragment fragment = MasterFragment.newInstance();
        fragment.setChangeListener(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_container, fragment)
                .commit();
    }

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
}
