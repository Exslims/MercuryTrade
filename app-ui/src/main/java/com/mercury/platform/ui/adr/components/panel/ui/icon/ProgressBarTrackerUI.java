package com.mercury.platform.ui.adr.components.panel.ui.icon;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconAlignment;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.BasicMercuryIconTrackerUI;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ProgressBarTrackerUI extends BasicMercuryIconTrackerUI<AdrProgressBarDescriptor> {
    public ProgressBarTrackerUI(AdrProgressBarDescriptor descriptor, MercuryTracker tracker) {
        super(descriptor,tracker);
    }

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
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g2.setColor(descriptor.getBackgroundColor());
        g2.fillRect(0,0,barRectWidth,barRectHeight);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        float sectorWidth = barRectWidth * tracker.getPercentComplete();
        Shape outer  = new Rectangle2D.Double(0, 0,barRectWidth, barRectHeight);
        Shape sector = new Rectangle2D.Double(0, 0, sectorWidth, barRectHeight);
        if(descriptor.isIconEnable()){
            int iconX = 0;
            int iconY = 0;
            if(descriptor.getIconAlignment().equals(AdrIconAlignment.RIGHT)){
                iconX = barRectWidth - barRectHeight;
            }
            sectorWidth = (barRectWidth - tracker.getHeight()) * tracker.getPercentComplete();
            if(descriptor.getIconAlignment().equals(AdrIconAlignment.RIGHT)) {
                outer = new Rectangle2D.Double(0, 0, barRectWidth - barRectHeight, barRectHeight);
                sector = new Rectangle2D.Double(0, 0, sectorWidth, barRectHeight);
            }else {
                outer = new Rectangle2D.Double(tracker.getHeight(), 0, barRectWidth, barRectHeight);
                sector = new Rectangle2D.Double(tracker.getHeight(), 0, sectorWidth, barRectHeight);
            }
            try {
                BufferedImage read = ImageIO.read(getClass().getClassLoader().getResource("app/adr/icons/" +descriptor.getIconPath() + ".png"));
                g2.drawImage(read,iconX,iconY,tracker.getHeight(),tracker.getHeight(),null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);
        g2.setPaint(descriptor.getForegroundColor());
        g2.fill(foreground);

        g2.dispose();
        this.paintString(g, 0, 0, barRectWidth, barRectHeight, 0);
        this.paintBorder(g);
    }
}
