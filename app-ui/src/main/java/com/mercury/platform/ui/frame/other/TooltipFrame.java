package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

public class TooltipFrame extends AbstractOverlaidFrame {
    private Timer tooltipTimer;

    public TooltipFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.componentsFactory.setScale(1.1f);
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.tooltipSubject.subscribe(tooltip -> {
            if (tooltip != null) {
                this.getContentPane().removeAll();
                this.setPreferredSize(null);
                if (tooltip.toCharArray().length < 85) {
                    JLabel tooltipLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 16f, tooltip);
                    this.add(tooltipLabel, BorderLayout.CENTER);
                } else {
                    JTextArea tooltipArea = componentsFactory.getSimpleTextArea("");
                    this.add(tooltipArea, BorderLayout.CENTER);
                    tooltipArea.setText(tooltip);
                    if (tooltip.toCharArray().length < 120) {
                        if (tooltip.toCharArray().length < 85) {
                            this.setPreferredSize(new Dimension(320, 55));
                        } else {
                            this.setPreferredSize(new Dimension(320, 75));
                        }
                    } else {
                        this.setPreferredSize(new Dimension(320, 145));
                    }
                }
                this.pack();
                this.repaint();
                this.tooltipTimer = new Timer(500, e -> {
                    Point cursorPoint = MouseInfo.getPointerInfo().getLocation();
                    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                    if (cursorPoint.y + this.getPreferredSize().height > dim.height) {
                        this.setLocation(cursorPoint.x + 4, cursorPoint.y - this.getPreferredSize().height);
                    } else {
                        this.setLocation(new Point(cursorPoint.x + 4, cursorPoint.y));
                    }
                    this.tooltipTimer.stop();
                    setVisible(true);
                });
                this.tooltipTimer.start();
            } else {
                if (tooltipTimer != null) {
                    this.tooltipTimer.stop();
                    this.setVisible(false);
                }
            }
        });
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    @Override
    public void onViewInit() {
        this.setOpacity(this.applicationConfig.get().getMaxOpacity() / 100f);
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 2)));
    }
}
