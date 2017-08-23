package com.mercury.platform.ui.adr.components.panel.ui.impl;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class VProgressBarTrackerUI extends MercuryProgressBarTrackerUI {
    protected void paintShapes(Graphics g, int barRectWidth, int barRectHeight, Insets insets) {
        int sectorX = 0;
        int sectorY = (int) (barRectHeight * (1f - this.tracker.getPercentComplete()));
        ;
        if (this.descriptor.isInvertMask()) {
            sectorY = 0;
        }
        Graphics2D g2 = this.prepareAdapter(g);
        float sectorHeight = barRectHeight * tracker.getPercentComplete();
        Shape outer = new Rectangle2D.Double(0, 0, barRectWidth, barRectHeight);
        Shape sector = new Rectangle2D.Double(insets.left + sectorX, insets.top + sectorY, barRectWidth - insets.right * 2, sectorHeight - insets.bottom * 2);
        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);
        g2.setPaint(descriptor.getForegroundColor());
        g2.fill(foreground);
        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight, 0);
    }
}

