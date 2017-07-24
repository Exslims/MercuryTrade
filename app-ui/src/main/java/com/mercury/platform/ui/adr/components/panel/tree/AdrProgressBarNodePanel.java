package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryTracker;

import javax.swing.*;
import java.awt.*;


public class AdrProgressBarNodePanel extends AdrNodePanel<AdrProgressBarDescriptor> {
    public AdrProgressBarNodePanel(AdrProgressBarDescriptor descriptor) {
        super(descriptor);
    }
    @Override
    public void createUI() {
        MercuryTracker tracker = new MercuryTracker(descriptor);
        tracker.setValue((int) ((descriptor.getDuration()/2) * 1000));
        tracker.setStringPainted(false);
        tracker.setPreferredSize(new Dimension(150, 26));
        this.setPreferredSize(new Dimension(150, 26));
        this.add(tracker,BorderLayout.CENTER);
    }
}
