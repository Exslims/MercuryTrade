package com.home.clicker.events.custom;

import com.home.clicker.events.SCEvent;

/**
 * Created by Константин on 17.12.2016.
 */
public class OpenHistoryEvent implements SCEvent {
    private int x;
    private int y;

    public OpenHistoryEvent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
