package com.mercury.platform.ui.adr.components.panel.ui;

import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.adr.components.panel.ui.icon.*;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;

import javax.swing.*;


public class MercuryTracker extends JComponent {
    @Getter
    private int value;
    @Setter @Getter
    private int maximum;
    @Setter @Getter
    private int minimum;
    @Setter @Getter
    private AdrDurationComponentDescriptor descriptor;
    @Setter @Getter
    private boolean stringPainted = true;
    @Setter @Getter
    private boolean maskPainted = true;
    @Setter @Getter
    private boolean showCase = false;

    private Timeline progressTl;

    public MercuryTracker(AdrDurationComponentDescriptor descriptor) {
        this.descriptor = descriptor;
        //todo remove
        this.setMaximum((int) (this.descriptor.getDuration() * 1000));
        this.setFont(new ComponentsFactory().getFont(FontStyle.BOLD, this.descriptor.getFontSize()));
        this.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.initUI();
        this.progressTl = new Timeline(this);
        this.progressTl.setDuration((int) (descriptor.getDuration() * 1000));
        if(this.descriptor.isInvertTimer()) {
            this.setValue((int) (this.descriptor.getDuration() * 1000));
            this.progressTl.addPropertyToInterpolate("value", 0, this.getMaximum());
        }else {
            this.setValue(0);
            this.progressTl.addPropertyToInterpolate("value", this.getMaximum(), 0);
        }
    }
    private void initUI(){
        switch (descriptor.getType()){
            case ICON: {
                if(((AdrIconDescriptor)descriptor).getIconType().equals(AdrIconType.SQUARE)){
                    this.setUI(new SquareIconTrackerUI((AdrIconDescriptor) descriptor,this));
                }else {
                    this.setUI(new EllipseIconTrackerUI((AdrIconDescriptor) descriptor,this));
                }
                break;
            }
            case PROGRESS_BAR: {
                if(descriptor.getOrientation().equals(AdrComponentOrientation.HORIZONTAL)) {
                    if(descriptor.isIconEnable()){
                        switch (((AdrProgressBarDescriptor) descriptor).getIconAlignment()){
                            case LEFT:{
                                this.setUI(new LIconHProgressBarTrackerUI((AdrProgressBarDescriptor) descriptor, this));
                                break;
                            }
                            case RIGHT:{
                                this.setUI(new RIconHProgressBarTrackerUI((AdrProgressBarDescriptor) descriptor, this));
                            }
                        }
                    }else {
                        this.setUI(new MercuryProgressBarTrackerUI((AdrProgressBarDescriptor) descriptor, this));
                    }
                }else {
                    if(descriptor.isIconEnable()){
                        switch (((AdrProgressBarDescriptor) descriptor).getIconAlignment()){
                            case TOP:{
                                this.setUI(new TIconVProgressBarTrackerUI((AdrProgressBarDescriptor) descriptor, this));
                                break;
                            }
                            case BOTTOM:{
                                this.setUI(new BIconVProgressBarTrackerUI((AdrProgressBarDescriptor) descriptor, this));
                            }
                        }
                    }else {
                        this.setUI(new VProgressBarTrackerUI((AdrProgressBarDescriptor) descriptor, this));
                    }
                }
                break;
            }
            default:{
                throw new IllegalArgumentException("AdrComponent for MercuryTracker must be ICON or PROGRESS_BAR");
            }
        }
    }

    public void setUI(BasicMercuryIconTrackerUI ui){
        super.setUI(ui);
    }

    public void setValue(int value) {
        this.value = value;
        this.updateUI();
    }

    @Override
    public void updateUI() {
        this.initUI();
        setUI(this.ui);
    }

    public float getPercentComplete(){
        long span = maximum - minimum;
        return (value - minimum) / (span * 1f);
    }
    public void addTimelineCallback(TimelineCallback callback){
        this.progressTl.addCallback(callback);
    }
    public void abort(){
        this.progressTl.abort();
    }
    public void playLoop(){
        this.progressTl.playLoop(Timeline.RepeatBehavior.LOOP);
    }
    public void play(){
        this.progressTl.play();
    }
    public void cancel(){
        this.progressTl.cancel();
    }
}
