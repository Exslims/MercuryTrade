package com.mercury.platform.ui.adr.components.factory;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.factory.frame.*;

import java.util.ArrayList;
import java.util.List;

public class FrameProviderFactory {
    private List<FrameProvider> providers = new ArrayList<>();

    public FrameProviderFactory() {
        this.providers.add(new IconFrameProvider());
        this.providers.add(new PBFrameProvider());
        this.providers.add(new TrackerGroupFrameProvider());
        this.providers.add(new CaptureInFrameProvider());
    }

    public FrameProvider getProviderFor(AdrComponentDescriptor descriptor) {
        return this.providers
                .stream()
                .filter(it -> it.isSuitable(descriptor))
                .findAny().orElse(null);
    }
}
