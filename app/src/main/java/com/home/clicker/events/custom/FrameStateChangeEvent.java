package com.home.clicker.events.custom;

import com.home.clicker.events.SCEvent;
import com.home.clicker.ui.FrameStates;

/**
 * Created by Константин on 09.12.2016.
 */
public class FrameStateChangeEvent implements SCEvent {
    private FrameStates state;

    public FrameStateChangeEvent(FrameStates state) {
        this.state = state;
    }

    public FrameStates getState() {
        return state;
    }

    public void setState(FrameStates state) {
        this.state = state;
    }
}
