package com.mercury.platform.ui.frame.adr.group;


import com.mercury.platform.shared.entity.adr.AdrCellDescriptor;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AddShowDelayEvent;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;

import java.awt.*;

public class AdrCellSettingsFrame extends AbstractTitledComponentFrame{
    private AdrCellDescriptor descriptor;

    public AdrCellSettingsFrame() {
        super("MercuryTrade");
        this.setFocusable(true);
        this.setFocusableWindowState(true);
        this.processingHideEvent = false;
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Cell settings";
    }

    @Override
    public void hideComponent() {
        super.hideComponent();
        EventRouter.CORE.fireEvent(new AddShowDelayEvent());
    }
    public void showSettings(AdrCellDescriptor descriptor, Point location){
        this.descriptor = descriptor;
        this.setLocation(location);
        this.showComponent();
    }
}
