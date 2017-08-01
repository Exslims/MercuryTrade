package com.mercury.platform.ui.adr.components.panel.ui.icon;


import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;

public interface ProgressBarUIStrategy {
    boolean isSuitable(AdrProgressBarDescriptor descriptor);
    MercuryProgressBarTrackerUI getUI();
}
