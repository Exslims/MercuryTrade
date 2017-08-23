package com.mercury.platform.ui.adr.components.panel.ui.impl;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class RIconHProgressBarTrackerUI extends MercuryProgressBarTrackerUI {
    @Override
    protected void paintShapes(Graphics g, int barRectWidth, int barRectHeight, Insets insets) {
        Graphics2D g2 = this.prepareAdapter(g);
        int iconX = barRectWidth - barRectHeight;
        int iconY = 0;
        float sectorWidth = (barRectWidth - tracker.getHeight()) * tracker.getPercentComplete();
        Shape outer = new Rectangle2D.Double(0, 0, barRectWidth - barRectHeight, barRectHeight);
        Shape sector = new Rectangle2D.Double(insets.left, insets.top, sectorWidth - insets.right * 2, barRectHeight - insets.bottom * 2);
        try {
            BufferedImage read = ImageIO.read(this.config.getIcon(descriptor.getIconPath()));
            g2.drawImage(read, iconX, iconY, tracker.getHeight(), tracker.getHeight(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);
        g2.setPaint(descriptor.getForegroundColor());
        g2.fill(foreground);
        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth - barRectHeight, barRectHeight, 0);
    }
}
