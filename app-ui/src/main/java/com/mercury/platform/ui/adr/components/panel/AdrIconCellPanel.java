package com.mercury.platform.ui.adr.components.panel;

import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.adr.components.panel.ui.icon.SquareMercuryIconTrackerUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallbackAdapter;

import javax.swing.*;
import java.awt.*;

public class AdrIconCellPanel extends AdrComponentPanel<AdrIconDescriptor>{
    private Timeline progressTl;
    public AdrIconCellPanel(AdrIconDescriptor cellDescriptor, ComponentsFactory componentsFactory) {
        super(cellDescriptor,componentsFactory);
        this.setLayout(new GridLayout(1,1));
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setPreferredSize(cellDescriptor.getSize());
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
        if(this.progressTl != null) {
            this.progressTl.cancel();
        }
        JProgressBar progressBar = new JProgressBar();
        progressBar.setBorder(null);
        progressBar.setFont(componentsFactory.getFont(FontStyle.BOLD,this.descriptor.getFontSize()));
        progressBar.setForeground(AppThemeColor.TEXT_DEFAULT);
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        progressBar.setBackground(AppThemeColor.TRANSPARENT);
        progressBar.setUI(new SquareMercuryIconTrackerUI(descriptor));
        progressBar.setValue(0);
        progressBar.setMaximum((int)(this.descriptor.getDuration() * 1000));
        this.add(progressBar,BorderLayout.CENTER);

        this.progressTl = new Timeline(progressBar);
        this.progressTl.setDuration((int)(descriptor.getDuration()*1000));
        this.progressTl.addPropertyToInterpolate("value",progressBar.getMaximum(),0);
        this.progressTl.addCallback(new TimelineCallbackAdapter() {
            @Override
            public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                if(newState.equals(Timeline.TimelineState.IDLE) && !inSettings){
                    setVisible(false);
                    MercuryStoreUI.adrRepaintSubject.onNext(true);
                }
            }
        });
        MercuryStoreUI.adrRepaintSubject.onNext(true);
        if(this.inSettings){
            this.enableSettings();
        }
    }
}
