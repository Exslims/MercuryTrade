package com.mercury.platform.ui.adr.components.panel.ui;

import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconType;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.icon.EllipseIconTrackerUI;
import com.mercury.platform.ui.adr.components.panel.ui.icon.ProgressBarTrackerUI;
import com.mercury.platform.ui.adr.components.panel.ui.icon.SquareIconTrackerUI;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;

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
    public MercuryTracker(AdrDurationComponentDescriptor descriptor) {
        this.descriptor = descriptor;
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
                this.setUI(new ProgressBarTrackerUI((AdrProgressBarDescriptor) descriptor,this));
                break;
            }
            default:{
                throw new IllegalArgumentException("AdrComponent for MercuryTracker must be ICON or PROGRESS_BAR");
            }
        }
        this.setValue(0);
        this.setMaximum((int) (this.descriptor.getDuration() * 1000));
        this.setFont(new ComponentsFactory().getFont(FontStyle.BOLD, this.descriptor.getFontSize()));
        this.setForeground(AppThemeColor.TEXT_DEFAULT);
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
        setUI(this.ui);
    }

    public float getPercentComplete(){
        long span = maximum - minimum;
        return (value - minimum) / (span * 1f);
    }
}
