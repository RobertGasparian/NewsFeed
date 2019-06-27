package com.example.newsfeed.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView webTitleTv, dateTv, bodyTv;
    private CheckBox checkBox;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pin_menu, menu);
        MenuItem checkboxItem = menu.findItem(R.id.pin_checkbox);
        FrameLayout checkboxLayout = (FrameLayout) checkboxItem.getActionView();
        checkBox = checkboxLayout.findViewById(R.id.checkbox);
        checkBox.setOnClickListener(v -> {
            if (model != null) {
                model.updatePinnedStatus();
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    protected void setupViews() {
        String transitionName = getArguments().getString(TRANSITION_NAME_KEY);
        thumbnailIv.setTransitionName(transitionName);
        bodyTv.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    protected void findViews(View view) {
        thumbnailIv = view.findViewById(R.id.thumbnail_iv);
        webTitleTv = view.findViewById(R.id.web_title_tv);
        dateTv = view.findViewById(R.id.date_tv);
        bodyTv = view.findViewById(R.id.body_tv);
    }

    @Override
    protected void bindModel() {
        model = ViewModelProviders.of(this).get(DetailViewModel.class);
        model.getDetailedNews(getArguments().getString(NEWS_ID_KEY)).observe(this, news -> {
            webTitleTv.setText(news.getWebTitle());
            dateTv.setText(news.getFormattedDate());
            checkBox.setChecked(news.isPinned());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                bodyTv.setText(Html.fromHtml(news.getFields() != null ? news.getFields().getBody() : getContext().getString(R.string.no_body_message), Html.FROM_HTML_MODE_COMPACT));
            } else {
                bodyTv.setText(Html.fromHtml(news.getFields() != null ? news.getFields().getBody() : getContext().getString(R.string.no_body_message)));
            }
            Glide.with(this).load(news.getFields() != null ? news.getFields().getThumbnail() : null).into(thumbnailIv);
        });
    }

    @Override
    protected void setupClicks() {
        //no clickable views
    }
}
