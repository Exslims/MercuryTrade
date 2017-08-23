package com.mercury.platform.ui.adr.components.factory.frame;

import com.mercury.platform.shared.config.descriptor.adr.AdrCaptureDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;
import com.mercury.platform.ui.adr.components.panel.AdrCapturePanel;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;
import com.mercury.platform.ui.components.ComponentsFactory;


public class CaptureInFrameProvider extends SingleFrameProvider {
    @Override
    public boolean isSuitable(AdrComponentDescriptor descriptor) {
        this.descriptor = descriptor;
        return descriptor.getType().equals(AdrComponentType.CAPTURE);
    }

    @Override
    public AdrComponentPanel getPanel() {
        return new AdrCapturePanel((AdrCaptureDescriptor) this.descriptor, new ComponentsFactory());
    }
}
