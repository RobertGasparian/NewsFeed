package com.example.newsfeed.fragments;



import android.os.Bundle;
import android.util.Log;
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
import com.example.newsfeed.adapters.LoadMoreListener;
import com.example.newsfeed.adapters.NewsFeedAdapter;
import com.example.newsfeed.adapters.NewsItemClickListener;
import com.example.newsfeed.adapters.PinnedAdapter;
import com.example.newsfeed.adapters.rvutils.LayoutMode;
import com.example.newsfeed.data.Repository;
import com.example.newsfeed.data.models.News;
import com.example.newsfeed.eventbus.ChangeCountEvent;
import com.example.newsfeed.eventbus.ReloadIfNeeded;
import com.example.newsfeed.fragments.base.BaseFragment;
import com.example.newsfeed.utils.AppExecutors;
import com.example.newsfeed.viewmodels.MasterViewModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MasterFragment extends BaseFragment implements NewsItemClickListener, LoadMoreListener {

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
                Repository.getInstance().setLayoutMode(getContext(), LayoutMode.LINEAR);
                newsFeedRV.setLayoutManager(new GridLayoutManager(getContext(), 1));
                newsFeedAdapter.notifyItemRangeChanged(0, newsFeedAdapter.getItemCount());
                break;
            case R.id.grid:
                Repository.getInstance().setLayoutMode(getContext(), LayoutMode.GRID);
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
        newsFeedAdapter.setLoadListener(this);
        String mode = Repository.getInstance().getLayoutMode(getContext());
        GridLayoutManager manager;
        if (mode.equals(LayoutMode.LINEAR.name())) {
            manager = new GridLayoutManager(getContext(), 1);
        } else if (mode.equals(LayoutMode.GRID.name())) {
            manager = new GridLayoutManager(getContext(), 2);
        } else {
            manager = new GridLayoutManager(getContext(), 1);
        }
        newsFeedRV.setLayoutManager(manager);
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
        model = ViewModelProviders.of(getActivity()).get(MasterViewModel.class);
        model.getNewsFeed().observe(this, news -> {
            newsFeedAdapter.submitList(news);
            AppExecutors.getInstance().diskIO().execute(() -> {
                int count = model.getNewsCount();
                AppExecutors.getInstance().mainThread().execute(() -> newsFeedAdapter.setDatabaseNewsCount(count));
            });
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

    @Override
    public void onLoadMore() {
        if (model != null) {
            AppExecutors.getInstance().diskIO().execute(() -> model.loadMore());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeCountEvent event) {
        if (newsFeedAdapter != null) {
            newsFeedAdapter.setDatabaseNewsCount(event.getCount());
        }
    }


    /**
     * Executes only when app is launched first time (DB is empty).
     * The reason is because when DB is empty and you observe LiveData, it does not trigger changes
     * after data was added into DB, so I need to tell my UI to observe one more time when data stored.
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReloadIfNeeded event) {
        bindModel();
    }
}
