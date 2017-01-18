package com.mercury.platform.shared.events.custom;

import com.mercury.platform.shared.events.MercuryEvent;

import java.awt.*;

/**
 * Created by Константин on 18.01.2017.
 */
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
