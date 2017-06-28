package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class FrameDescriptor {
    private Point frameLocation;
    private Dimension frameSize;
}
