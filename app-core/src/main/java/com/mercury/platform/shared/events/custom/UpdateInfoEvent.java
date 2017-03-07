package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

/**
 * Created by Константин on 07.03.2017.
 */
public class UpdateInfoEvent implements MercuryEvent {
    private int version;

    public UpdateInfoEvent(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
