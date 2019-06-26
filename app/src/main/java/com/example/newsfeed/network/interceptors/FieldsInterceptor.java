package com.example.newsfeed.network.interceptors;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class FieldsInterceptor implements Interceptor {

    private static final String FIELDS = "thumbnail,body";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder().addQueryParameter("show-fields",FIELDS).build();
        request = request.newBuilder().url(url).build();
        Response response = chain.proceed(request);
        return response;
    }
}
