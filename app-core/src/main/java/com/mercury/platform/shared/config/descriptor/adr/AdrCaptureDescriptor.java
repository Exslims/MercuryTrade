package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrCaptureDescriptor extends AdrColoredComponentDescriptor implements Serializable {
    private int fps = 5;
    private Dimension captureSize = new Dimension(64, 64);
    private Point captureLocation;
}
