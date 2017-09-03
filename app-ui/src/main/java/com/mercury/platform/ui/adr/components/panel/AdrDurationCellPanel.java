package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.core.ProdStarter;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupType;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class AdrDurationCellPanel extends AdrDurationComponentPanel<AdrDurationComponentDescriptor> {
    private MercuryTracker tracker;

    public AdrDurationCellPanel(AdrDurationComponentDescriptor cellDescriptor, ComponentsFactory componentsFactory) {
        super(cellDescriptor, componentsFactory);
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(null);
        this.setVisible(false);
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.setVisible(true);
        this.tracker.abort();
        this.tracker.setValue(new Random().nextInt((int) (this.descriptor.getDuration() * 1000)));
        this.tracker.setStringPainted(descriptor.isTextEnable());
        this.tracker.setMaskPainted(descriptor.isMaskEnable());
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.tracker.abort();
        this.setVisible(this.descriptor.isAlwaysVisible());
        this.tracker.setStringPainted(!descriptor.isAlwaysVisible());
        this.tracker.setMaskPainted(!descriptor.isAlwaysVisible());
    }

    @Override
    public void onSelect() {
        this.tracker.playLoop();
    }

    @Override
    public void onUnSelect() {
        this.tracker.abort();
    }

    @Override
    protected void onHotKeyPressed() {
        if (ProdStarter.APP_STATUS.equals(FrameVisibleState.SHOW)) {
            this.tracker.setStringPainted(descriptor.isTextEnable());
            this.tracker.setMaskPainted(descriptor.isMaskEnable());
            if (this.descriptor.isHotKeyRefresh()) {
                this.tracker.abort();
            }
            this.tracker.play();
            if (this.getParent() instanceof AdrTrackerGroupPanel) {
                AdrTrackerGroupPanel parent = (AdrTrackerGroupPanel) this.getParent();
                if (parent.getDescriptor().getGroupType().equals(AdrTrackerGroupType.DYNAMIC)) {
                    parent.setComponentZOrder(this, parent.getComponentCount() - 1);
                }
            }
        }
    }

    @Override
    protected void onUpdate() {
        this.tracker.abort();
        this.tracker.setVisible(false);
        this.remove(this.tracker);
        this.onViewInit();
    }

    @Override
    public void onViewInit() {
        this.setLayout(new GridLayout(1, 1));
        this.setPreferredSize(this.descriptor.getSize());
        this.tracker = new MercuryTracker(this.descriptor);
        this.add(this.tracker);
        this.tracker.addTimelineCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (newState.equals(Timeline.TimelineState.IDLE) && !inSettings) {
                    if (!descriptor.isAlwaysVisible()) {
                        setVisible(false);
                    } else {
                        tracker.setStringPainted(!descriptor.isAlwaysVisible());
                        tracker.setMaskPainted(!descriptor.isAlwaysVisible());
                    }
                    MercuryStoreUI.adrRepaintSubject.onNext(true);
                } else if (newState.equals(Timeline.TimelineState.PLAYING_FORWARD)) {
                    setVisible(true);
                    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(AdrDurationCellPanel.this);
                    parent.pack();
                }
            }
        });
        MercuryStoreUI.adrRepaintSubject.onNext(true);
    }
}
