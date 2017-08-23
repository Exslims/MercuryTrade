package com.mercury.platform.ui.adr.components.factory.frame;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.AbstractAdrFrame;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;

public interface FrameProvider {
    boolean isSuitable(AdrComponentDescriptor descriptor);

    AbstractAdrFrame getFrame(boolean showSettings);

    AdrComponentPanel getPanel();
}
