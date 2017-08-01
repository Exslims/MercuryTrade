package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrProgressBarDescriptor extends AdrDurationComponentDescriptor implements Serializable {
    private Color foregroundColor = new Color(16,91,99);
    private AdrIconAlignment iconAlignment = AdrIconAlignment.LEFT;
}
