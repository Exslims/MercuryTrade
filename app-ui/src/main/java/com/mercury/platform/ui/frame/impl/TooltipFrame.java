package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.HideTooltipEvent;
import com.mercury.platform.shared.events.custom.ShowTooltipEvent;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.manager.HideSettingsManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 17.01.2017.
 */
public class TooltipFrame extends OverlaidFrame {
    private Timer tooltipTimer;
    public TooltipFrame() {
        super("MercuryTrade");
        this.processingHideEvent = false;
        this.setAlwaysOnTop(true);
        this.setOpacity(ConfigManager.INSTANCE.getMaxOpacity()/100f);
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2)));
    }

    @Override
    protected void initialize() {
    }
    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(ShowTooltipEvent.class, event -> {
            getContentPane().removeAll();
            this.setPreferredSize(null);
            String tooltip = ((ShowTooltipEvent) event).getTooltip();
            if(tooltip.toCharArray().length < 85){
                JLabel tooltipLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP,16f,tooltip);
                this.add(tooltipLabel,BorderLayout.CENTER);
            }else {
                JTextArea tooltipArea = componentsFactory.getSimpleTextAre("");
                this.add(tooltipArea,BorderLayout.CENTER);
                tooltipArea.setText(tooltip);
                if(tooltip.toCharArray().length < 120){
                    if(tooltip.toCharArray().length < 85){
                        this.setPreferredSize(new Dimension(320, 50));
                    }else {
                        this.setPreferredSize(new Dimension(320, 70));
                    }
                }else {
                    this.setPreferredSize(new Dimension(320, 110));
                }
            }
            this.pack();
            this.repaint();
            tooltipTimer = new Timer(500, e -> {
                Point cursorPoint = MouseInfo.getPointerInfo().getLocation();
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                if(cursorPoint.y + this.getPreferredSize().height > dim.height){
                    this.setLocation(cursorPoint.x + 4,cursorPoint.y - this.getPreferredSize().height);
                }else {
                    this.setLocation(new Point(cursorPoint.x + 4,cursorPoint.y));
                }
                tooltipTimer.stop();
                setVisible(true);
            });
            tooltipTimer.start();
        });
        EventRouter.INSTANCE.registerHandler(HideTooltipEvent.class, event -> {
            if(tooltipTimer != null) {
                tooltipTimer.stop();
                setVisible(false);
            }
        });
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
