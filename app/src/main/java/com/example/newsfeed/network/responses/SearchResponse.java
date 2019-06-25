package com.example.newsfeed.network.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResponse {

    @SerializedName("response")
    @Expose
    private NewsFeedResponse response;

    public NewsFeedResponse getResponse() {
        return response;
    }

    public void setResponse(NewsFeedResponse response) {
        this.response = response;
    }
}
