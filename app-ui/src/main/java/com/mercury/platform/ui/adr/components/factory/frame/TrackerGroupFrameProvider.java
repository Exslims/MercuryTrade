package com.mercury.platform.ui.adr.components.factory.frame;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.ui.adr.components.AbstractAdrFrame;
import com.mercury.platform.ui.adr.components.AdrTrackerGroupFrame;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;
import com.mercury.platform.ui.adr.components.panel.AdrTrackerGroupPanel;
import com.mercury.platform.ui.components.ComponentsFactory;


public class TrackerGroupFrameProvider implements FrameProvider {
    private AdrComponentDescriptor descriptor;

    @Override
    public boolean isSuitable(AdrComponentDescriptor descriptor) {
        this.descriptor = descriptor;
        return descriptor.getType().equals(AdrComponentType.TRACKER_GROUP);
    }

    @Override
    public AbstractAdrFrame getFrame(boolean showSettings) {
        AdrTrackerGroupFrame adrTrackerGroupFrame =
                new AdrTrackerGroupFrame((AdrTrackerGroupDescriptor) descriptor);
        adrTrackerGroupFrame.setPanel(this.getPanel());
        adrTrackerGroupFrame.init();
        if (showSettings) {
            adrTrackerGroupFrame.showComponent();
            adrTrackerGroupFrame.enableSettings();
        } else {
            adrTrackerGroupFrame.disableSettings();
        }
        return adrTrackerGroupFrame;
    }

    @Override
    public AdrComponentPanel getPanel() {
        return new AdrTrackerGroupPanel((AdrTrackerGroupDescriptor) this.descriptor, new ComponentsFactory());
    }
}
