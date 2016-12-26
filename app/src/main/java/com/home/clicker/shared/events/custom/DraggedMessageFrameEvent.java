package com.home.clicker.shared.events.custom;

import com.home.clicker.shared.events.SCEvent;

/**
 * Created by Константин on 24.12.2016.
 */
public class DraggedMessageFrameEvent implements SCEvent {
    private int x;
    private int y;

    public DraggedMessageFrameEvent(int x, int y) {
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
