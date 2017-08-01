package com.mercury.platform.ui.adr.components.panel.ui.icon;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TIconVProgressBarTrackerUI extends VProgressBarTrackerUI{
    @Override
    protected void paintShapes(Graphics g, int barRectWidth, int barRectHeight, Insets insets) {
        if(tracker.isShowCase()){
            super.paintShapes(g,barRectWidth,barRectHeight,insets);
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        int iconX = 0;
        int iconY = 0;
        float sectorHeight = (barRectHeight - tracker.getWidth()) * tracker.getPercentComplete();
        Shape outer  = new Rectangle2D.Double(0, 0,barRectWidth, barRectHeight);
        Shape sector = new Rectangle2D.Double(insets.left, insets.top + tracker.getWidth(), barRectWidth - insets.right * 2, sectorHeight - insets.bottom * 2);
        try {
            BufferedImage read = ImageIO.read(this.config.getIcon(descriptor.getIconPath()));
            g2.drawImage(read,iconX,iconY,tracker.getWidth(),tracker.getWidth(),null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);
        g2.setPaint(descriptor.getForegroundColor());
        g2.fill(foreground);
        g2.dispose();
        this.paintString(g, 0, this.tracker.getWidth(), barRectWidth, barRectHeight - tracker.getWidth(), 0);
    }
}
