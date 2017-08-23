package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.config.descriptor.adr.AdrCaptureDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import org.imgscalr.Scalr;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class AdrCapturePanel extends AdrComponentPanel<AdrCaptureDescriptor> {
    private Timeline progressTl;
    private JLabel captureLabel;

    public AdrCapturePanel(AdrCaptureDescriptor groupDescriptor, ComponentsFactory componentsFactory) {
        super(groupDescriptor, componentsFactory);
    }

    @Override
    public void onViewInit() {
        this.setLayout(new GridLayout(1, 1));
        this.setPreferredSize(this.descriptor.getSize());
        this.captureLabel = new JLabel();
        this.setBackground(AppThemeColor.ADR_CAPTURE_BG);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
        this.progressTl = new Timeline(this);
        this.progressTl.addPropertyToInterpolate("captureCount", 0, this.descriptor.getFps());
        this.captureLabel.setIcon(new ImageIcon(Scalr.resize(getCapture(), descriptor.getSize().width, descriptor.getSize().height)));
        this.progressTl.addCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                captureLabel.setIcon(new ImageIcon(Scalr.resize(getCapture(), descriptor.getSize().width, descriptor.getSize().height)));
            }
        });
        this.progressTl.setDuration(1000 / this.descriptor.getFps());

        this.add(this.captureLabel);
        MercuryStoreUI.adrRepaintSubject.onNext(true);
    }


    @Override
    public void enableSettings() {
        super.enableSettings();
        this.setVisible(true);
        this.progressTl.abort();
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.progressTl.playLoop(Timeline.RepeatBehavior.LOOP);
        this.setVisible(this.descriptor.isVisible());
    }

    @Override
    public void onSelect() {
        this.progressTl.playLoop(Timeline.RepeatBehavior.LOOP);
    }

    @Override
    public void onUnSelect() {
        this.progressTl.abort();
    }

    @Override
    protected void onUpdate() {
        this.setVisible(this.descriptor.isVisible());
        this.progressTl.abort();
        this.progressTl.setDuration(1000 / this.descriptor.getFps());
        this.progressTl.playLoop(Timeline.RepeatBehavior.LOOP);
    }

    public void setCaptureCount(int captureCount) {
    }

    private BufferedImage getCapture() {
        try {
            Robot robot = new Robot();
            Dimension size = this.descriptor.getCaptureSize();
            Point location = this.descriptor.getCaptureLocation();
            return robot.createScreenCapture(new Rectangle(location.x + 5, location.y + 5, size.width - 10, size.height - 10));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return null;
    }
}
