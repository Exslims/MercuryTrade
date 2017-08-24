package com.mercury.platform.ui.adr.components.panel.ui;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.IconBundleConfigurationService;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentOrientation;
import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import lombok.Setter;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.text.DecimalFormat;


public abstract class BasicMercuryIconTrackerUI<T extends AdrDurationComponentDescriptor> extends ComponentUI {
    @Setter
    protected T descriptor;
    @Setter
    protected MercuryTracker tracker;
    protected IconBundleConfigurationService config;

    protected BasicMercuryIconTrackerUI() {
        this.config = Configuration.get().iconBundleConfiguration();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dimension = super.getPreferredSize(c);
        if (dimension == null) {
            dimension = new Dimension(150, 26);
        }
        int value = Math.max(dimension.width, dimension.height);
        dimension.setSize(value, value);
        return dimension;
    }

    protected void paintString(Graphics g, int x, int y, int width, int height, int amountFull) {

        if (descriptor.isTextEnable() && tracker.isStringPainted()) {
            float value = tracker.getValue() / 1000f;

            Graphics2D g2 = this.prepareAdapter(g);
            DecimalFormat decimalFormat = new DecimalFormat(descriptor.getTextFormat());
            String progressString = String.valueOf(decimalFormat.format(value));
            if (descriptor.isCustomTextEnable()) {
                progressString = String.valueOf(descriptor.getCustomText());
            }
            g2.setFont(tracker.getFont());
            Point renderLocation = getStringPlacement(g2, progressString,
                    x, y, width, height);
            g2.setColor(this.getColorByValue());
            SwingUtilities2.drawString(tracker, g2, progressString,
                    renderLocation.x, renderLocation.y);
            if (this.descriptor.getOutlineThickness() > 0) {
                FontRenderContext frc = g2.getFontRenderContext();
                TextLayout textTl = new TextLayout(progressString, tracker.getFont(), frc);
                Shape outline = textTl.getOutline(null);
                g2.translate(renderLocation.x, renderLocation.y);
                Stroke oldStroke = g2.getStroke();
                g2.setStroke(new BasicStroke(this.descriptor.getOutlineThickness()));
                Color oldColor = g2.getColor();
                g2.setColor(this.descriptor.getOutlineColor());
                g2.draw(outline);
                g2.setColor(oldColor);
                g2.setStroke(oldStroke);
            }
        }
    }

    protected void paintBorder(Graphics g) {
        int thickness = descriptor.getThickness();
        if (thickness > 0) {
            Graphics2D g2 = this.prepareAdapter(g);
            g2.setColor(getColorByValue());
            Stroke oldStroke = g2.getStroke();
            if (!descriptor.isBindToTextColor()) {
                g2.setPaint(this.descriptor.getBorderColor());
            }
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRect(0, 0, tracker.getWidth() - thickness, tracker.getHeight() - thickness);
            g2.setStroke(oldStroke);
        }
    }

    private Color getColorByValue() {
        float value = tracker.getValue() / 1000f;
        if (value >= descriptor.getDefaultValueTextThreshold()) {
            return this.descriptor.getDefaultValueTextColor();
        } else if (value >= this.descriptor.getMediumValueTextThreshold()) {
            return this.descriptor.getMediumValueTextColor();
        } else {
            return this.descriptor.getLowValueTextColor();
        }
    }

    protected Point getStringPlacement(Graphics g, String progressString,
                                       int x, int y, int width, int height) {
        FontMetrics fontSizer = SwingUtilities2.getFontMetrics(tracker, g,
                tracker.getFont());
        int stringWidth = SwingUtilities2.stringWidth(tracker, fontSizer,
                progressString);

        if (descriptor.getOrientation() == AdrComponentOrientation.HORIZONTAL) {
            return new Point(x + Math.round(width / 2 - stringWidth / 2),
                    y + ((height +
                            fontSizer.getAscent() -
                            fontSizer.getLeading() -
                            fontSizer.getDescent()) / 2));
        } else {
            return new Point(x + ((width -
                    fontSizer.getAscent() -
                    fontSizer.getLeading() -
                    fontSizer.getDescent()) / 2),
                    y + ((height +
                            fontSizer.getAscent() -
                            fontSizer.getLeading() -
                            fontSizer.getDescent()) / 2));
        }
    }

    protected Graphics2D prepareAdapter(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g2;
    }
}
