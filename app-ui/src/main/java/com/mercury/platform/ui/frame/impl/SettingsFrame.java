package com.mercury.platform.ui.frame.impl;


import com.mercury.platform.ui.frame.OverlaidFrame;

import java.awt.*;

/**
 * Created by Константин on 16.12.2016.
 */
public class SettingsFrame extends OverlaidFrame {
    public SettingsFrame(){
        super("MT-Settings");
        setFocusableWindowState(true);
        setFocusable(true);

        pack();
        disableHideEffect();
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Settings";
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
