package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class ShowTooltipEvent implements MercuryEvent {
    private String tooltip;
    private Point cursorPoint;
}
