package com.mercury.platform.ui.components.panel.misc;

import com.mercury.platform.ui.adr.components.panel.ui.MercuryLoading;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MercuryAppLoadingUI extends ComponentUI {
    private MercuryLoading component;

    public MercuryAppLoadingUI(MercuryLoading component) {
        this.component = component;
    }

    public ImageIcon getIcon(String iconPath, int size) {
        BufferedImage icon = null;
        try {
            BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource(iconPath));
            icon = Scalr.resize(buttonIcon, size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon(icon);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        if (component.getWidth() == 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        double degree = 360 * component.getPercentComplete();
        double size = 120;
        double iconSize = 46;
        double outerR = size * .5;
        double bgR = outerR * 1.04;
        double innerR = outerR * .78;
        double pointX = component.getWidth() * .5;
        double pointY = component.getHeight() * .5;
        Shape inner = new Ellipse2D.Double(pointX - innerR, pointY - innerR, innerR * 2, innerR * 2);
        Shape outer = new Ellipse2D.Double(pointX - outerR, pointY - outerR, size, size);
        Shape bg = new Ellipse2D.Double(pointX - bgR, pointY - bgR, bgR * 2, bgR * 2);
        Shape sector = new Arc2D.Double(pointX - outerR, pointY - outerR, size, size, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);
        Area bgArea = new Area(bg);
        Area hole = new Area(inner);

        foreground.subtract(hole);
        background.subtract(hole);

        g2.setPaint(AppThemeColor.ADR_FOOTER_BG);
        g2.fill(bgArea);
        g2.setPaint(this.component.getBackground());
        g2.drawImage(this.getIcon("app/app-icon-big.png", (int) iconSize * 2).getImage(), (int) (pointX - iconSize), (int) (pointY - iconSize), null);
        g2.fill(background);

        g2.setPaint(component.getForeground());
        g2.fill(foreground);
        g2.dispose();
    }
}
