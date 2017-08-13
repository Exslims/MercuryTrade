package com.mercury.platform.ui.adr.components.factory.frame;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;


public class IconFrameProvider extends SingleFrameProvider {
    @Override
    public boolean isSuitable(AdrComponentDescriptor descriptor) {
        this.descriptor = descriptor;
        return descriptor.getType().equals(AdrComponentType.ICON);
    }
}
