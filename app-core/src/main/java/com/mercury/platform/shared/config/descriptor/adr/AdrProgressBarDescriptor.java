package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrProgressBarDescriptor extends AdrDurationComponentDescriptor implements Serializable {
    private Color backgroundColor;
    private Color foregroundColor;
    private Insets insets = new Insets(4,4,4,4);
    private AdrIconAlignment iconAlignment;
}
