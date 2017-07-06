package com.mercury.platform.shared.entity.adr;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrGroupDescriptor extends AdrComponentDescriptor {
    private Dimension iconSize;
    private List<AdrIconDescriptor> cells;
    public AdrGroupDescriptor(Dimension iconSize, Point location) {
        super(location,iconSize);
    }
}
