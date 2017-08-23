package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrColoredComponentDescriptor extends AdrComponentDescriptor implements Serializable {
    private Color backgroundColor = new Color(59, 59, 59);
    private Color foregroundColor = new Color(59, 59, 59, 190);
    private Color borderColor = new Color(16, 110, 99);
    private int thickness = 1;
}
