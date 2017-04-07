package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class SaveScaleEvent implements MercuryEvent {
    private Map<String,Float> scaleData;
}
