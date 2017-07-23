package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrProgressBarDescriptor extends AdrDurationComponentDescriptor {
    private boolean iconEnable;
    private Color backgroundColor;
    private Color foregroundColor;
    private Color borderColor;
    private int insets;
    private int borderSize;
}
