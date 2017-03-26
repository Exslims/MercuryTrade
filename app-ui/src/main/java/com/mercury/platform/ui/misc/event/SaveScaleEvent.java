package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.ui.misc.data.ScaleData;

public class SaveScaleEvent implements MercuryEvent {
    private ScaleData scaleData;

    public SaveScaleEvent(ScaleData scaleData) {
        this.scaleData = scaleData;
    }

    public ScaleData getScaleData() {
        return scaleData;
    }
}
