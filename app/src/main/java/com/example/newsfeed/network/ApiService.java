package com.example.newsfeed.network;

import com.example.newsfeed.network.responses.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search")
    Call<Response> getFeedData(@Query("page") int page);
}
