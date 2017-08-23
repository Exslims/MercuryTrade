package com.mercury.platform.ui.adr.components.panel.ui.impl;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.BasicMercuryIconTrackerUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class EllipseIconTrackerUI extends BasicMercuryIconTrackerUI<AdrIconDescriptor> {
    @Override
    public void paint(Graphics g, JComponent c) {
        Insets b = tracker.getInsets();
        int barRectWidth = tracker.getWidth();
        int barRectHeight = tracker.getHeight();
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g.setColor(tracker.getBackground());
        g2.fillRect(0, 0, barRectWidth, barRectHeight);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        double degree = 360 * (1f - tracker.getPercentComplete());
        double sz = Math.max(barRectWidth, barRectHeight) - 2;
        Shape outer = new Ellipse2D.Double(1, 1, sz, sz);
        Shape sector = new Arc2D.Double(-sz, -sz, sz * 3, sz * 3, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);

        g2.setPaint(new Color(59, 59, 59));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        try {
            BufferedImage read = ImageIO.read(getClass().getClassLoader().getResource("app/adr/icons/" + descriptor.getIconPath() + ".png"));

            Shape imageEllipse = new Ellipse2D.Double(1, 1, sz, sz);
            g2.setClip(imageEllipse);
            g2.drawImage(read, 0, 0, (int) sz, (int) sz, null);
            g2.setClip(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.fill(foreground);
        this.paintOvalBorder(g, barRectWidth, barRectHeight);
        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight, 0);
    }

    private void paintOvalBorder(Graphics g, int width, int height) {
        int thickness = descriptor.getThickness();
        if (thickness > 0) {
            Graphics2D g2 = (Graphics2D) g;
            Stroke oldStroke = g2.getStroke();
            if (!descriptor.isBindToTextColor()) {
                g2.setPaint(this.descriptor.getBorderColor());
            }
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));
            g2.setStroke(new BasicStroke(thickness + 1));
            g2.drawOval(1, 1, width - 1, height - 1);
            g2.setStroke(oldStroke);
        }
    }
}
