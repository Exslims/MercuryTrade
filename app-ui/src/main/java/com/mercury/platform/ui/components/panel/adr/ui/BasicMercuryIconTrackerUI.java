package com.mercury.platform.ui.components.panel.adr.ui;

import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.text.DecimalFormat;


public abstract class BasicMercuryIconTrackerUI extends BasicProgressBarUI{
    @Getter
    @Setter
    protected String iconPath;
    protected BasicMercuryIconTrackerUI(String iconPath){
        this.iconPath = iconPath;
    }
    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dimension = super.getPreferredSize(c);
        int value = Math.max(dimension.width, dimension.height);
        dimension.setSize(value, value);
        return dimension;
    }

    @Override
    protected void paintString(Graphics g, int x, int y, int width, int height, int amountFull, Insets b) {
        float value = progressBar.getValue() / 1000f;

        Graphics2D g2 = (Graphics2D)g;
        DecimalFormat decimalFormat = new DecimalFormat("0");
        String progressString = String.valueOf(decimalFormat.format(value));
        g2.setFont(progressBar.getFont());
        Point renderLocation = getStringPlacement(g2, progressString,
                x, y, width, height);
        Rectangle oldClip = g2.getClipBounds();
        if(value > 4f) {
            g2.setColor(getSelectionForeground());
        }else if(value < 4f && value > 1f) {
            g2.setColor(AppThemeColor.TEXT_NICKNAME);
        }else {
            g2.setColor(AppThemeColor.TEXT_IMPORTANT);
        }
        SwingUtilities2.drawString(progressBar, g2, progressString,
                renderLocation.x, renderLocation.y);
        g2.setColor(getSelectionForeground());
        g2.clipRect(width, y, amountFull, height);
        SwingUtilities2.drawString(progressBar, g2, progressString,
                renderLocation.x, renderLocation.y);
        g2.setClip(oldClip);
    }
}
