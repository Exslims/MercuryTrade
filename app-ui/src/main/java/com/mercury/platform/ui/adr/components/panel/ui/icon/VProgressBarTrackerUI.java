package com.mercury.platform.ui.adr.components.panel.ui.icon;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class VProgressBarTrackerUI extends MercuryProgressBarTrackerUI {
    protected void paintShapes(Graphics g, int barRectWidth, int barRectHeight, Insets insets) {
        Graphics2D g2 = (Graphics2D) g.create();
        float sectorHeight = barRectHeight * tracker.getPercentComplete();
        Shape outer  = new Rectangle2D.Double(0, 0,barRectWidth, barRectHeight);
        Shape sector = new Rectangle2D.Double(insets.left, insets.top, barRectWidth - insets.right * 2, sectorHeight - insets.bottom * 2);
        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);
        g2.setPaint(descriptor.getForegroundColor());
        g2.fill(foreground);
        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight, 0);
    }
}

