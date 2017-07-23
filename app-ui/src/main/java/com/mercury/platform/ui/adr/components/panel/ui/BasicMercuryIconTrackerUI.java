package com.mercury.platform.ui.adr.components.panel.ui;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentOrientation;
import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.text.DecimalFormat;


public abstract class BasicMercuryIconTrackerUI<T extends AdrDurationComponentDescriptor> extends ComponentUI{
    protected T descriptor;
    protected MercuryTracker tracker;

    protected BasicMercuryIconTrackerUI(T descriptor, MercuryTracker tracker){
        this.descriptor = descriptor;
        this.tracker = tracker;
    }
    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dimension = super.getPreferredSize(c);
        int value = Math.max(dimension.width, dimension.height);
        dimension.setSize(value, value);
        return dimension;
    }

    protected void paintString(Graphics g, int x, int y, int width, int height, int amountFull) {
        if(descriptor.isTextEnable()) {
            float value = tracker.getValue() / 1000f;

            Graphics2D g2 = (Graphics2D) g;
            DecimalFormat decimalFormat = new DecimalFormat(descriptor.getTextFormat());
            String progressString = String.valueOf(decimalFormat.format(value));
            g2.setFont(tracker.getFont());
            Point renderLocation = getStringPlacement(g2, progressString,
                    x, y, width, height);
            Rectangle oldClip = g2.getClipBounds();
            if (value >= descriptor.getDefaultValueTextThreshold()) {
                g2.setColor(this.descriptor.getDefaultValueTextColor());
            } else if (value >= this.descriptor.getMediumValueTextThreshold()) {
                g2.setColor(this.descriptor.getMediumValueTextColor());
            } else {
                g2.setColor(this.descriptor.getLowValueTextColor());
            }
            SwingUtilities2.drawString(tracker, g2, progressString,
                    renderLocation.x, renderLocation.y);
            g2.clipRect(width, y, amountFull, height);
            SwingUtilities2.drawString(tracker, g2, progressString,
                    renderLocation.x, renderLocation.y);
            g2.setClip(oldClip);
        }
    }
    protected void paintBorder(Graphics g){
        int thickness = descriptor.getThickness();
        if(thickness > 0) {
            Graphics2D g2 = (Graphics2D) g;
            Stroke oldStroke = g2.getStroke();
            if(!descriptor.isBindToTextColor()) {
                g2.setPaint(this.descriptor.getBorderColor());
            }
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRect(0, 0, tracker.getWidth() - thickness, tracker.getHeight() - thickness);
            g2.setStroke(oldStroke);
        }
    }

    protected Point getStringPlacement(Graphics g, String progressString,
                                       int x,int y,int width,int height) {
        FontMetrics fontSizer = SwingUtilities2.getFontMetrics(tracker, g,
                tracker.getFont());
        int stringWidth = SwingUtilities2.stringWidth(tracker, fontSizer,
                progressString);

        if (descriptor.getOrientation() == AdrComponentOrientation.HORIZONTAL) {
            return new Point(x + Math.round(width/2 - stringWidth/2),
                    y + ((height +
                            fontSizer.getAscent() -
                            fontSizer.getLeading() -
                            fontSizer.getDescent()) / 2));
        } else {
            return new Point(x + ((width - fontSizer.getAscent() +
                    fontSizer.getLeading() + fontSizer.getDescent()) / 2),
                    y + Math.round(height/2 - stringWidth/2));
        }
    }
}
