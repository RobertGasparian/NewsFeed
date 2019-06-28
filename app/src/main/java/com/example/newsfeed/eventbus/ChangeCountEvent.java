package com.example.newsfeed.eventbus;

public class ChangeCountEvent {

    private int count;

    public ChangeCountEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
