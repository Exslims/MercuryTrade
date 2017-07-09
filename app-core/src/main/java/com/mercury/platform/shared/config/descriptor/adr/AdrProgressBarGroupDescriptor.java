package com.mercury.platform.shared.config.descriptor.adr;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrProgressBarGroupDescriptor extends AdrComponentDescriptor {
    private List<AdrProgressBarGroupDescriptor> cells;
    public AdrProgressBarGroupDescriptor(Point location, Dimension size) {
        super(location, size);
    }
}
