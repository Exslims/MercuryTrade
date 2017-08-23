package com.mercury.platform.ui.adr.components.panel.ui.impl;


import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.BasicMercuryIconTrackerUI;
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
        if (!tracker.isShowCase()) {
            if (!descriptor.isVisible()) {
                return;
            }
        }
        int barRectWidth = tracker.getWidth();
        int barRectHeight = tracker.getHeight();
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(descriptor.getBackgroundColor().getAlpha() == 0 ? AlphaComposite.CLEAR : AlphaComposite.SRC));
        if (this.tracker.isShowCase()) {
            if (descriptor.getBackgroundColor().getAlpha() < 10) {
                g2.setColor(AppThemeColor.ADR_TEXT_ARE_BG);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
            } else {
                g2.setColor(descriptor.getBackgroundColor());
            }
        } else {
            g2.setColor(descriptor.getBackgroundColor());
        }
        g2.fillRect(0, 0, barRectWidth, barRectHeight);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double sz = Math.max(barRectWidth, barRectHeight);
        if (descriptor.isIconEnable()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            try {
                BufferedImage read = ImageIO.read(this.config.getIcon(descriptor.getIconPath()));
                g2.drawImage(read, this.descriptor.getInsets().left, this.descriptor.getInsets().top,
                        (int) sz - this.descriptor.getInsets().bottom, (int) sz - this.descriptor.getInsets().right, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (tracker.isMaskPainted() && descriptor.isMaskEnable() && !tracker.isShowCase()) {
            double degree;
            if (descriptor.isInvertMask()) {
                degree = 360 * tracker.getPercentComplete();
            } else {
                degree = 360 * (1f - tracker.getPercentComplete());
            }
            Shape outer = new Rectangle2D.Double(0, 0, sz, sz);
            Shape sector = new Arc2D.Double(-sz, -sz, sz * 3, sz * 3, 90 - degree, degree, Arc2D.PIE);

            Area foreground = new Area(sector);
            Area background = new Area(outer);

            foreground.intersect(background);

            g2.setPaint(this.descriptor.getForegroundColor());
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2.fill(foreground);
        }

        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight, 0);
        this.paintBorder(g);
    }
}
