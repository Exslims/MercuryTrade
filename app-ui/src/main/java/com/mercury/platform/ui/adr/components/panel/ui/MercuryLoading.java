package com.mercury.platform.ui.adr.components.panel.ui;

import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;
import org.pushingpixels.trident.ease.Spline;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;


public class MercuryLoading extends JComponent {
    @Getter
    private int value;
    @Setter
    @Getter
    private int maximum;
    @Setter
    @Getter
    private int minimum;
    private Timeline progressTl;
    @Setter
    private boolean swapEnable;

    public MercuryLoading() {
        this.setValue(0);
        this.setMaximum(3000);
        this.setForeground(AppThemeColor.TEXT_NICKNAME);
        this.setBackground(AppThemeColor.ADR_FOOTER_BG);

        this.setUI(new MercuryLoadingUi(this));

        this.progressTl = new Timeline(this);
        this.progressTl.setDuration(2400);
        this.progressTl.addPropertyToInterpolate("value", this.getMaximum(), 0);
        this.progressTl.setEase(new Spline(1));
        this.progressTl.addCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                swapColors();
            }
        });
    }

    public void setValue(int value) {
        this.value = value;
        this.updateUI();
    }

    public void setLoadingUI(ComponentUI componentUI) {
        this.setUI(componentUI);
    }

    @Override
    public void updateUI() {
        setUI(this.ui);
    }

    public float getPercentComplete() {
        long span = maximum - minimum;
        return (value - minimum) / (span * 1f);
    }

    public void abort() {
        this.progressTl.abort();
    }

    public void playLoop() {
        this.progressTl.playLoop(Timeline.RepeatBehavior.LOOP);
    }

    public void play() {
        this.progressTl.play();
    }

    public void cancel() {
        this.progressTl.cancel();
    }

    public void swapColors() {
        if (swapEnable) {
            Color temp = new Color(this.getBackground().getRGB());
            this.setBackground(this.getForeground());
            this.setForeground(temp);
        }
    }
}
