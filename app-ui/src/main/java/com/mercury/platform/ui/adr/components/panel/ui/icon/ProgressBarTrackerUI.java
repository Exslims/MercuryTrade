package com.mercury.platform.ui.adr.components.panel.ui.icon;

import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.BasicMercuryIconTrackerUI;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class ProgressBarTrackerUI extends BasicMercuryIconTrackerUI<AdrProgressBarDescriptor> {
    public ProgressBarTrackerUI(AdrProgressBarDescriptor descriptor, MercuryTracker tracker) {
        super(descriptor,tracker);
    }


    public void paint(Graphics g, JComponent c) {
        Insets b = tracker.getInsets();
        int barRectWidth  = tracker.getWidth();
        int barRectHeight = tracker.getHeight();
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g2.setColor(descriptor.getBackgroundColor());
        g2.fillRect(0,0,barRectWidth,barRectHeight);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        Shape outer  = new Rectangle2D.Double(0, 0,barRectWidth, barRectHeight);
        Shape sector = new Rectangle2D.Double(0, 0, barRectWidth * tracker.getPercentComplete(), barRectHeight);
        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);

        g2.setPaint(descriptor.getForegroundColor());
//        try {
//            BufferedImage read = ImageIO.read(getClass().getClassLoader().getResource("app/adr/icons/" +descriptor.getIconPath() + ".png"));
//
//            Shape imageEllipse  = new Ellipse2D.Double(1, 1, sz, sz);
//            g2.setClip(imageEllipse);
//            g2.drawImage(read,0,0,(int)sz,(int)sz,null);
//            g2.setClip(null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
        g2.fill(foreground);
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC,1f));
//        g2.setPaint(AppThemeColor.MSG_HEADER_BORDER);
//        g2.setStroke(new BasicStroke(2));

        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight, 0, b);
    }
}
