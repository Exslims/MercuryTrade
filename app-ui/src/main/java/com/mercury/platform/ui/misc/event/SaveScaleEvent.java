package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import com.mercury.platform.shared.pojo.ScaleData;

import java.util.Map;

public class SaveScaleEvent implements MercuryEvent {
    private Map<String,Float> scaleData;

    public SaveScaleEvent(Map<String,Float> scaleData) {
        this.scaleData = scaleData;
    }

    public Map<String,Float> getScaleData() {
        return scaleData;
    }
}
