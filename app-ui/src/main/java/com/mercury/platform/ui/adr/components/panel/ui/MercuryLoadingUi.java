package com.mercury.platform.ui.adr.components.panel.ui;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;


public class MercuryLoadingUi extends ComponentUI {
    private MercuryLoading component;

    public MercuryLoadingUi(MercuryLoading component) {
        this.component = component;
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
        double size = 60;
        double outerR = size * .5;
        double innerR = outerR * .78;
        double pointX = component.getWidth() * .5;
        double pointY = component.getHeight() * .5;
        Shape inner = new Ellipse2D.Double(pointX - innerR, pointY - innerR, innerR * 2, innerR * 2);
        Shape outer = new Ellipse2D.Double(pointX - outerR, pointY - outerR, size, size);
        Shape sector = new Arc2D.Double(pointX - outerR, pointY - outerR, size, size, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);
        Area hole = new Area(inner);

        foreground.subtract(hole);
        background.subtract(hole);

        g2.setPaint(this.component.getBackground());
        g2.fill(background);

        g2.setPaint(component.getForeground());
        g2.fill(foreground);
        g2.dispose();
    }
}
