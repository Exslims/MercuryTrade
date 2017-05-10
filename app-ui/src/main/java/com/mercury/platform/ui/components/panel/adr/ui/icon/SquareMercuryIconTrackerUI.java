package com.mercury.platform.ui.components.panel.adr.ui.icon;


import com.mercury.platform.ui.components.panel.adr.ui.BasicMercuryIconTrackerUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SquareMercuryIconTrackerUI extends BasicMercuryIconTrackerUI {
    public SquareMercuryIconTrackerUI(String iconPath) {
        super(iconPath);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets();
        int barRectWidth  = progressBar.getWidth();
        int barRectHeight = progressBar.getHeight();
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g.setColor(progressBar.getBackground());
        g2.fillRect(0,0,barRectWidth,barRectHeight);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        double degree = 360 * (1f - progressBar.getPercentComplete());
        double sz = Math.max(barRectWidth, barRectHeight);
        Shape outer  = new Rectangle2D.Double(0, 0, sz, sz);
        Shape sector = new Arc2D.Double(-sz, -sz, sz *3, sz *3, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);

        g2.setPaint(new Color(59, 59, 59));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        try {
            BufferedImage read = ImageIO.read(getClass().getClassLoader().getResource("app/adr/" +iconPath + ".png"));
            g2.drawImage(read,0,0,(int)sz,(int)sz,null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.7f));
        g2.fill(foreground);

        g2.dispose();
        if (progressBar.isStringPainted()) {
            paintString(g, 0, 0, barRectWidth, barRectHeight, 0, b);
        }
    }
}
