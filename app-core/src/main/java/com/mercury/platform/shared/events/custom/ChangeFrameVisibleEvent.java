package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 10.12.2016.
 */
public class ChangeFrameVisibleEvent implements MercuryEvent {
    private FrameStates states;

    public ChangeFrameVisibleEvent(FrameStates states) {
        this.states = states;
    }

    public FrameStates getStates() {
        return states;
    }
}
