package com.mercury.platform.shared.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class FrameSettings {
    private Point frameLocation;
    private Dimension frameSize;
}
