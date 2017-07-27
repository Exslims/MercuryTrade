package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.adr.components.panel.AdrTrackerGroupPanel;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;


public class AdrTrackerGroupFrame extends AbstractAdrComponentFrame<AdrTrackerGroupDescriptor> {
    private AdrTrackerGroupPanel trackerGroupPanel;
    public AdrTrackerGroupFrame(@NonNull AdrTrackerGroupDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.trackerGroupPanel = new AdrTrackerGroupPanel(this.descriptor,this.componentsFactory);
        this.add(this.trackerGroupPanel,BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void subscribe() {
        super.subscribe();
        MercuryStoreUI.adrReloadSubject.subscribe(descriptor -> {
            if(descriptor.equals(this.descriptor)){
                this.setLocation(descriptor.getLocation());
                this.setOpacity(this.descriptor.getOpacity());
                this.repaint();
                this.pack();
            }
        });
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.trackerGroupPanel.enableSettings();
        this.trackerGroupPanel.addMouseListener(this.mouseListener);
        this.trackerGroupPanel.addMouseListener(this.mouseOverListener);
        this.trackerGroupPanel.addMouseMotionListener(this.motionListener);
        this.pack();
        this.repaint();
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.trackerGroupPanel.disableSettings();
        this.trackerGroupPanel.removeMouseListener(this.mouseListener);
        this.trackerGroupPanel.removeMouseListener(this.mouseOverListener);
        this.trackerGroupPanel.removeMouseMotionListener(this.motionListener);
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        this.pack();
        this.repaint();
    }
}
