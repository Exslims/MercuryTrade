package com.mercury.platform.ui.components.fields;

import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Created by Константин on 04.01.2017.
 */
public class ExSliderUI extends BasicSliderUI {
    public ExSliderUI(JSlider b) {
        super(b);
    }

    public static ExSliderUI createUI(JComponent c) {
        return new ExSliderUI((JSlider)c);
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int x1 = thumbRect.x + 2;
        int x2 = thumbRect.x + thumbRect.width - 2;
        int width = thumbRect.width - 4;
        int topY = thumbRect.y + thumbRect.height / 2 - thumbRect.width / 3;
        GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        shape.moveTo(x1, topY);
        shape.lineTo(x2, topY);
        shape.lineTo((x1 + x2) / 2, topY + width);
        shape.closePath();
        g2d.setPaint(AppThemeColor.BUTTON);
        g2d.fill(shape);
        Stroke old = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2f));
        g2d.setPaint(new Color(131, 127, 211));
        g2d.draw(shape);
        g2d.setStroke(old);
    }
}
