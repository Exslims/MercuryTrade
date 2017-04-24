package com.mercury.platform.ui.frame.adr;

import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;

public class AdrManagerFrame extends AbstractTitledComponentFrame {
    public AdrManagerFrame() {
        super("MercuryTrade");
        this.setFocusableWindowState(true);
        this.setFocusable(true);
        this.setType(Type.UTILITY);
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "placeholder";
    }
}
