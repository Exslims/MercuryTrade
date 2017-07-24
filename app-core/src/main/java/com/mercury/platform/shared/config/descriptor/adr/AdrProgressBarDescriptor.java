package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrProgressBarDescriptor extends AdrDurationComponentDescriptor {
    private Color backgroundColor;
    private Color foregroundColor;
    private AdrIconAlignment iconAlignment;
}
