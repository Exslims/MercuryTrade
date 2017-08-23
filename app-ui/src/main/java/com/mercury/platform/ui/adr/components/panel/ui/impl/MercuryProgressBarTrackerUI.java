package com.mercury.platform.ui.adr.components.panel.ui.impl;

import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.BasicMercuryIconTrackerUI;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;


public class MercuryProgressBarTrackerUI extends BasicMercuryIconTrackerUI<AdrProgressBarDescriptor> {
    @Override
    public void paint(Graphics g, JComponent c) {
        if (!tracker.isShowCase()) {
            if (!descriptor.isVisible()) {
                return;
            }
        }
        Insets insets = descriptor.getInsets();
        int barRectWidth = tracker.getWidth();
        int barRectHeight = tracker.getHeight();
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = this.prepareAdapter(g);
        g2.fillRect(0, 0, barRectWidth, barRectHeight);
        this.paintShapes(g, barRectWidth, barRectHeight, insets);
        this.paintBorder(g);
    }

    protected void paintShapes(Graphics g, int barRectWidth, int barRectHeight, Insets insets) {
        Graphics2D g2 = (Graphics2D) g.create();
        float sectorWidth = barRectWidth * tracker.getPercentComplete();
        Shape outer = new Rectangle2D.Double(0, 0, barRectWidth, barRectHeight);
        Shape sector = new Rectangle2D.Double(insets.left, insets.top, sectorWidth - insets.right * 2, barRectHeight - insets.bottom * 2);
        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);
        g2.setPaint(descriptor.getForegroundColor());
        g2.fill(foreground);
        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight, 0);
    }

    @Override
    protected Graphics2D prepareAdapter(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        if (this.tracker.isShowCase()) {
            if (descriptor.getBackgroundColor().getAlpha() < 10) {
                g2.setColor(AppThemeColor.ADR_TEXT_ARE_BG);
            } else {
                g2.setColor(descriptor.getBackgroundColor());
            }
        } else {
            g2.setColor(descriptor.getBackgroundColor());
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        return g2;
    }
}
