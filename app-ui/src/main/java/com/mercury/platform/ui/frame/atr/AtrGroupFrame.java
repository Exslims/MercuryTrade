package com.mercury.platform.ui.frame.atr;


import com.mercury.platform.shared.entity.atr.AtrGroupSettings;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import lombok.Getter;

import java.awt.*;

public class AtrGroupFrame extends AbstractOverlaidFrame{
    @Getter
    private AtrGroupSettings atrGroupSettings;

    public AtrGroupFrame(AtrGroupSettings settings) {
        super("MercuryTrade");
        this.atrGroupSettings = settings;
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return null;
    }

    @Override
    protected void initialize() {

    }
}
