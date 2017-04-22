package com.mercury.platform.ui.frame.adr;

import com.mercury.platform.ui.frame.AbstractOverlaidFrame;

public abstract class AbstractAdrFrame extends AbstractOverlaidFrame{
    protected AbstractAdrFrame(String title) {
        super(title);
    }
    public abstract void enableSettings();
    public abstract void disableSettings();
}
