package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.HideTooltipEvent;
import com.mercury.platform.shared.events.custom.ShowTooltipEvent;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.manager.HideSettingsManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 17.01.2017.
 */
public class TooltipFrame extends OverlaidFrame {
    private JLabel tooltipLabel;
    private Timer tooltipTimer;
    public TooltipFrame() {
        super("MT-Tooltips");
        this.processingHideEvent = false;
        this.setVisible(false);
        this.setAlwaysOnTop(true);
        prevState = FrameStates.HIDE;
        this.setOpacity(ConfigManager.INSTANCE.getMaxOpacity()/100f);
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));
    }

    @Override
    protected void initialize() {
        tooltipLabel = componentsFactory.getTextLabel("");
        this.add(tooltipLabel);
    }
    @Override
    public void initHandlers() {
        //todo
        EventRouter.INSTANCE.registerHandler(ShowTooltipEvent.class, event -> {
            String tooltip = ((ShowTooltipEvent) event).getTooltip();
            Point cursorPoint = ((ShowTooltipEvent) event).getCursorPoint();
            setLocation(cursorPoint);
            tooltipLabel.setText(tooltip);
            pack();
            repaint();
            tooltipTimer = new Timer(500, e -> {
                tooltipTimer.stop();
                setVisible(true);
            });
            tooltipTimer.setInitialDelay(1200);
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
        return new FlowLayout(FlowLayout.LEFT);
    }
}
