package com.example.newsfeed.eventbus;

public class ErrorEvent {

    private String msg;

    public ErrorEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
