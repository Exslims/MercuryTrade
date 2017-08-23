package com.mercury.platform.ui.adr.components.panel.ui.impl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class BIconVProgressBarTrackerUI extends VProgressBarTrackerUI {
    @Override
    protected void paintShapes(Graphics g, int barRectWidth, int barRectHeight, Insets insets) {
        if (tracker.isShowCase()) {
            super.paintShapes(g, barRectWidth, barRectHeight, insets);
            return;
        }
        Graphics2D g2 = this.prepareAdapter(g);
        int iconX = 0;
        int iconY = barRectHeight - barRectWidth;
        int sectorX = 0;
        int sectorY = (int) ((barRectHeight - barRectWidth) * (1f - this.tracker.getPercentComplete()));
        if (this.descriptor.isInvertMask()) {
            sectorY = (int) ((barRectHeight - barRectWidth) * this.tracker.getPercentComplete());
        }
        float sectorHeight = barRectHeight - sectorY - barRectWidth;
        Shape outer = new Rectangle2D.Double(0, 0, barRectWidth, barRectHeight);
        Shape sector = new Rectangle2D.Double(insets.left + sectorX, insets.top + sectorY, barRectWidth - insets.right * 2, sectorHeight - insets.bottom * 2);
        try {
            BufferedImage read = ImageIO.read(this.config.getIcon(descriptor.getIconPath()));
            g2.drawImage(read, iconX, iconY, tracker.getWidth(), tracker.getWidth(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);
        g2.setPaint(descriptor.getForegroundColor());
        g2.fill(foreground);
        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight - barRectWidth, 0);
    }
}
