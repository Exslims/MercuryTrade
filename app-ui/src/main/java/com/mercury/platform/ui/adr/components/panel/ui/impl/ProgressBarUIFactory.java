package com.mercury.platform.ui.adr.components.panel.ui.impl;


import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;

public interface ProgressBarUIFactory {
    boolean isSuitable(AdrProgressBarDescriptor descriptor);

    MercuryProgressBarTrackerUI getUI();
}
