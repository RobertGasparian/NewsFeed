package com.example.newsfeed.network.interceptors;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {

    private static final String API_KEY = "e4b4c383-d893-4efb-966f-6fa5180a44d0";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("api-key", API_KEY)
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}
