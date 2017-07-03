package com.mercury.platform.shared.entity.adr;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrGroupDescriptor extends AdrComponentDescriptor {
    private Point location = new Point(400,400);
    private Dimension iconSize = new Dimension(64,64);
    private List<AdrIconDescriptor> cells;
}
