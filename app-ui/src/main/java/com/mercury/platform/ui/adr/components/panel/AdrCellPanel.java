package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;
import com.mercury.platform.ui.adr.components.panel.ui.icon.ProgressBarTrackerUI;
import com.mercury.platform.ui.adr.components.panel.ui.icon.EllipseIconTrackerUI;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.adr.components.panel.ui.icon.SquareIconTrackerUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;

import java.awt.*;

public class AdrCellPanel extends AdrComponentPanel<AdrDurationComponentDescriptor>{
    private Timeline progressTl;
    public AdrCellPanel(AdrDurationComponentDescriptor cellDescriptor, ComponentsFactory componentsFactory) {
        super(cellDescriptor,componentsFactory);
        this.setLayout(new GridLayout(1,1));
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(null);
        this.createUI();
        this.setVisible(false);
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.setVisible(true);
        this.progressTl.playLoop(Timeline.RepeatBehavior.LOOP);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.progressTl.abort();
        this.setVisible(false);
    }

    @Override
    protected void onHotKeyPressed() {
        this.setVisible(true);
        this.progressTl.play();
    }

    @Override
    public void createUI() {
        this.setPreferredSize(this.descriptor.getSize());
        if (this.progressTl != null) {
            this.progressTl.cancel();
        }
        MercuryTracker tracker = new MercuryTracker(this.descriptor);
        this.add(tracker, BorderLayout.CENTER);

        this.progressTl = new Timeline(tracker);
        this.progressTl.setDuration((int) (descriptor.getDuration() * 1000));
        this.progressTl.addPropertyToInterpolate("value", tracker.getMaximum(), 0);
        this.progressTl.addCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if (newState.equals(Timeline.TimelineState.IDLE) && !inSettings) {
                    setVisible(false);
                    MercuryStoreUI.adrRepaintSubject.onNext(true);
                }
            }
        });
        MercuryStoreUI.adrRepaintSubject.onNext(true);
        if (this.inSettings) {
            this.enableSettings();
        }
    }
}
