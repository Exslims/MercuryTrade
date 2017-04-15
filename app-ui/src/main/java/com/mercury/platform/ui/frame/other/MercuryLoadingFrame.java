package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

import javax.swing.*;
import java.awt.*;


public class MercuryLoadingFrame extends AbstractOverlaidFrame {
    private Timeline hideAnimation;
    private Timeline showAnimation;
    public MercuryLoadingFrame() {
        super("MercuryTrade");
        this.setOpacity(0f);
        this.setBackground(AppThemeColor.TRANSPARENT);
        processingHideEvent = false;
    }

    @Override
    protected void initialize() {
        JLabel icon = componentsFactory.getIconLabel("app/app-icon-big.png");

        hideAnimation = new Timeline(this);
        hideAnimation.setDuration(400);
        hideAnimation.addPropertyToInterpolate("opacity", 1f, 0f);
        hideAnimation.addCallback(new TimelineCallback() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if(newState.equals(Timeline.TimelineState.DONE)){
                    setAlwaysOnTop(false);
                    setVisible(false);
                }
            }
            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
            }
        });

        showAnimation = new Timeline(this);
        showAnimation.setDuration(400);
        showAnimation.addPropertyToInterpolate("opacity", 0f, 1f);
        this.add(icon);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    @Override
    public void initHandlers() {
        MercuryStore.INSTANCE.appLoadingSubject
                .subscribe(state -> hideAnimation.play());
    }

    @Override
    public void showComponent() {
        super.showComponent();
        showAnimation.play();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new FlowLayout();
    }
}
