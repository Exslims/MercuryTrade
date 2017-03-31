package com.mercury.platform.shared.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;

@Data
@AllArgsConstructor
public class FrameSettings {
    private Point frameLocation;
    private Dimension frameSize;
}
