package com.mercury.platform.ui.adr;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdrFrameMagnet {
    public static AdrFrameMagnet INSTANCE = AdrFrameMagnetHolder.HOLDER_INSTANCE;
    @Setter
    private List<AdrComponentDescriptor> descriptors;
    @Setter
    private int delta;

    public void obtainApproxFrameLocation(Point source, AdrComponentDescriptor sourceDescriptor) {
        this.descriptors
                .stream()
                .filter(descriptor -> !descriptor.equals(sourceDescriptor))
                .collect(Collectors.toList()).forEach(descriptor -> {
            if ((descriptor.getLocation().x + descriptor.getSize().width + delta) > source.x) {
                source.x = descriptor.getLocation().x + descriptor.getSize().width;
                if ((descriptor.getLocation().y + descriptor.getSize().height + delta) > source.y) {
                    source.y = descriptor.getLocation().y + descriptor.getSize().height;
                }
            }
        });
    }

    private static class AdrFrameMagnetHolder {
        static final AdrFrameMagnet HOLDER_INSTANCE = new AdrFrameMagnet();
    }
}
