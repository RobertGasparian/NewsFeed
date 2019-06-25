package com.example.newsfeed.network;

import com.example.newsfeed.network.interceptors.ApiKeyInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://content.guardianapis.com/";

    public static ApiService getApiService() {
        if (retrofit==null) {
            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new ApiKeyInterceptor());

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
