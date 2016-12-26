package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.SCEvent;

/**
 * Created by Константин on 10.12.2016.
 */
public class ChangeFrameVisibleEvent implements SCEvent {
    private FrameStates states;

    public ChangeFrameVisibleEvent(FrameStates states) {
        this.states = states;
    }

    public FrameStates getStates() {
        return states;
    }
}
