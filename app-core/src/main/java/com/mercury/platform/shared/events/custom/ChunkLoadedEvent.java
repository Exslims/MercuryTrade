package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 07.03.2017.
 */
public class ChunkLoadedEvent implements MercuryEvent {
    private int percent;

    public ChunkLoadedEvent(int percent) {
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
