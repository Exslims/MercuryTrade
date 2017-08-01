package com.mercury.platform.ui.adr.components.panel.ui.icon;


import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.BasicMercuryIconTrackerUI;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SquareIconTrackerUI extends BasicMercuryIconTrackerUI<AdrIconDescriptor> {
    @Override
    public void paint(Graphics g, JComponent c) {
        if(!tracker.isShowCase()){
            if(!descriptor.isVisible()) {
                return;
            }
        }
        int barRectWidth  = tracker.getWidth();
        int barRectHeight = tracker.getHeight();
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(tracker.getBackground().equals(AppThemeColor.TRANSPARENT)? AlphaComposite.CLEAR : AlphaComposite.SRC_OVER));
        g2.setColor(tracker.getBackground());
        g2.fillRect(0,0,barRectWidth,barRectHeight);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double sz = Math.max(barRectWidth, barRectHeight);
        if(!descriptor.getIconPath().equals("no_icon.png")) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            try {
                BufferedImage read = ImageIO.read(this.config.getIcon(descriptor.getIconPath()));
                g2.drawImage(read, 0, 0, (int) sz, (int) sz, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(tracker.isMaskPainted() && descriptor.isMaskEnable() && !tracker.isShowCase()) {
            double degree;
            if(descriptor.isInvertMask()) {
                degree = 360 * tracker.getPercentComplete();
            }else {
                degree = 360 * (1f - tracker.getPercentComplete());
            }
            Shape outer = new Rectangle2D.Double(0, 0, sz, sz);
            Shape sector = new Arc2D.Double(-sz, -sz, sz * 3, sz * 3, 90 - degree, degree, Arc2D.PIE);

            Area foreground = new Area(sector);
            Area background = new Area(outer);

            foreground.intersect(background);

            g2.setPaint(new Color(59, 59, 59));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g2.fill(foreground);
        }

        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight, 0);
        this.paintBorder(g);
    }
}
