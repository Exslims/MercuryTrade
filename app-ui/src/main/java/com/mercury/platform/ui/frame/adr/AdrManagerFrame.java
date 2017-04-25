package com.mercury.platform.ui.frame.adr;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AddShowDelayEvent;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;

import javax.swing.*;
import java.awt.*;

public class AdrManagerFrame extends AbstractTitledComponentFrame{
    public AdrManagerFrame() {
        super("MercuryTrade");
    }

    @Override
    protected void initialize() {
        super.initialize();

    }
    private JPanel getRootPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        return root;
    }

    @Override
    public void initHandlers() {
        add(componentsFactory.getTextField("123"));
    }

    @Override
    protected String getFrameTitle() {
        return "placeholder";
    }

}
