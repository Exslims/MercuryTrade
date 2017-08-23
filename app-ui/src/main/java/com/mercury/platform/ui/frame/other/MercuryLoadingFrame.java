package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryLoading;
import com.mercury.platform.ui.components.panel.misc.MercuryAppLoadingUI;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

import java.awt.*;


public class MercuryLoadingFrame extends AbstractOverlaidFrame {
    private Timeline hideAnimation;
    private Timeline showAnimation;
    private MercuryLoading loadingTracker;

    public MercuryLoadingFrame() {
        super();
        processingHideEvent = false;
    }

    @Override
    protected void initialize() {
        this.setOpacity(0f);
        this.setBackground(AppThemeColor.TRANSPARENT);
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.appLoadingSubject
                .subscribe(state -> {
                    hideAnimation.play();
                });
        MercuryStoreCore.errorHandlerSubject.subscribe(error -> {
            this.loadingTracker.abort();
        });
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

    @Override
    public void onViewInit() {
        this.loadingTracker = new MercuryLoading();
        this.loadingTracker.setSwapEnable(true);
        this.loadingTracker.setLoadingUI(new MercuryAppLoadingUI(this.loadingTracker));
        this.loadingTracker.playLoop();
        this.loadingTracker.setForeground(AppThemeColor.ADR_FOOTER_BG);
        this.loadingTracker.setBackground(AppThemeColor.BORDER_GREEN);
        this.loadingTracker.setPreferredSize(new Dimension(200, 200));
        hideAnimation = new Timeline(this);
        hideAnimation.setDuration(400);
        hideAnimation.addPropertyToInterpolate("opacity", 1f, 0f);
        hideAnimation.addCallback(new TimelineCallback() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (newState.equals(Timeline.TimelineState.DONE)) {
                    setAlwaysOnTop(false);
                    setVisible(false);
                    loadingTracker.abort();
                }
            }

            @Override
            public void onTimelinePulse(float durationFraction, float timelinePosition) {
            }
        });

        showAnimation = new Timeline(this);
        showAnimation.setDuration(400);
        showAnimation.addPropertyToInterpolate("opacity", 0f, 1f);
        this.add(this.loadingTracker);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }
}
