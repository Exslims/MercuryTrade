package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.MercuryEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangeFrameVisibleEvent implements MercuryEvent {
    private FrameStates states;
}
