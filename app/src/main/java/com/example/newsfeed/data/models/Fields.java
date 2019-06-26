package com.example.newsfeed.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("body")
    @Expose
    private String body;

    public Fields() {}

    public Fields(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Fields(String thumbnail, String body) {
        this.thumbnail = thumbnail;
        this.body = body;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
