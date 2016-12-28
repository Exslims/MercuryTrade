package com.mercury.platform.ui;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.NotificationEvent;
import com.mercury.platform.ui.components.fields.label.FontStyle;
import com.mercury.platform.ui.components.fields.label.TextAlignment;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 29.12.2016.
 */
public class NotificationFrame extends OverlaidFrame {
    private JLabel strokeLabel;
    private Timeline showAnimation;
    protected NotificationFrame() {
        super("Notification frame");
    }

    @Override
    protected void init() {
        super.init();
        this.setOpacity(0.9f);
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.getRootPane().setBorder(null);

        strokeLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT, TextAlignment.CENTER,38,"");

        showAnimation = new Timeline(this);
        showAnimation.setDuration(1400);
        showAnimation.addPropertyToInterpolate("opacity", 0.9f, 0f);
        showAnimation.addCallback(new TimelineCallback() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if(newState.equals(Timeline.TimelineState.DONE)){
                    NotificationFrame.this.setVisible(false);
                    NotificationFrame.this.setOpacity(0.9f);
                    strokeLabel.setText("");
                }
            }
            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
            }
        });
        this.add(strokeLabel);
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(NotificationEvent.class, event -> {
            strokeLabel.setText(((NotificationEvent) event).getStroke());
            NotificationFrame.this.pack();
            NotificationFrame.this.setVisible(true);
            showAnimation.abort();
            showAnimation.play();
        });
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new FlowLayout();
    }
}
