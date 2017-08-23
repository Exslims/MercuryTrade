package com.mercury.platform.ui.adr.components.factory.frame;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrDurationComponentDescriptor;
import com.mercury.platform.ui.adr.components.AbstractAdrFrame;
import com.mercury.platform.ui.adr.components.AdrSingleComponentFrame;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;
import com.mercury.platform.ui.adr.components.panel.AdrDurationCellPanel;
import com.mercury.platform.ui.components.ComponentsFactory;

public abstract class SingleFrameProvider implements FrameProvider {
    protected AdrComponentDescriptor descriptor;

    @Override
    public AbstractAdrFrame getFrame(boolean showSettings) {
        AdrSingleComponentFrame frame = new AdrSingleComponentFrame(descriptor);
        frame.setPanel(this.getPanel());
        frame.init();
        if (showSettings) {
            frame.showComponent();
            frame.enableSettings();
        } else {
            frame.disableSettings();
        }
        return frame;
    }

    @Override
    public AdrComponentPanel getPanel() {
        return new AdrDurationCellPanel((AdrDurationComponentDescriptor) this.descriptor, new ComponentsFactory());
    }
}
