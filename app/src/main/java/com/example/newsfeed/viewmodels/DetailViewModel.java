package com.example.newsfeed.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import com.example.newsfeed.data.Repository;
import com.example.newsfeed.data.models.News;

public class DetailViewModel extends AndroidViewModel {

    private MutableLiveData<String> idLD = new MutableLiveData<>();
    private LiveData<News> newsLD = Transformations.switchMap(idLD, id -> Repository.getInstance().getNewsById(id));
    private Observer<News> updateObserver;

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<News> getDetailedNews(String id) {
        idLD.setValue(id);
        return newsLD;
    }

    public void updatePinnedStatus() {
        updateObserver = news -> {
            news.setPinned(!news.isPinned());
            Repository.getInstance().updateNews(news);
            removeUpdateObs();
        };
        newsLD.observeForever(updateObserver);
    }

    private void removeUpdateObs() {
        if (updateObserver != null && newsLD != null) {
            newsLD.removeObserver(updateObserver);
        }
    }
}
