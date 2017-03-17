package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;


public class UpdateInfoEvent implements MercuryEvent {
    private int version;

    public UpdateInfoEvent(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
