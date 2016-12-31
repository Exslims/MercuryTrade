package com.mercury.platform.shared.pojo;

import java.awt.*;

/**
 * Created by Константин on 31.12.2016.
 */
public class FrameSettings {
    private Point frameLocation;
    private Dimension frameSize;

    public FrameSettings(Point frameLocation, Dimension frameSize) {
        this.frameLocation = frameLocation;
        this.frameSize = frameSize;
    }

    public Point getFrameLocation() {
        return frameLocation;
    }

    public void setFrameLocation(Point frameLocation) {
        this.frameLocation = frameLocation;
    }

    public Dimension getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(Dimension frameSize) {
        this.frameSize = frameSize;
    }
}
