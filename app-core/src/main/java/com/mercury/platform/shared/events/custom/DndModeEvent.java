package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 27.01.2017.
 */
public class DndModeEvent implements MercuryEvent {
    private boolean dnd;

    public DndModeEvent(boolean dnd) {
        this.dnd = dnd;
    }

    public boolean isDnd() {
        return dnd;
    }
}
