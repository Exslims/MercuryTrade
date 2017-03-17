package com.mercury.platform.ui.misc.event;

import com.mercury.platform.shared.events.MercuryEvent;

import java.awt.*;


public class ShowTooltipEvent implements MercuryEvent {
    private String tooltip;
    private Point cursorPoint;

    public ShowTooltipEvent(String tooltip, Point cursorPoint) {
        this.tooltip = tooltip;
        this.cursorPoint = cursorPoint;
    }

    public String getTooltip() {
        return tooltip;
    }

    public Point getCursorPoint() {
        return cursorPoint;
    }
}
