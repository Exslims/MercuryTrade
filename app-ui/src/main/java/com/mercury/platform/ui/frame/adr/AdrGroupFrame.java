package com.mercury.platform.ui.frame.adr;


import com.mercury.platform.shared.entity.adr.AdrGroupSettings;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import lombok.Getter;

import java.awt.*;

public class AdrGroupFrame extends AbstractOverlaidFrame{
    @Getter
    private AdrGroupSettings adrGroupSettings;

    public AdrGroupFrame(AdrGroupSettings settings) {
        super("MercuryTrade");
        this.adrGroupSettings = settings;
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
