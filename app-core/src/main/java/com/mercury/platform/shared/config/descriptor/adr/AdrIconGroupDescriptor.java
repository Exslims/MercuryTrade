package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrIconGroupDescriptor extends AdrComponentDescriptor {
    private List<AdrIconDescriptor> cells;
    public AdrIconGroupDescriptor(Dimension iconSize, Point location) {
        super(location,iconSize);
    }
}
