package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.frame.TitledComponentFrame;

import java.awt.*;

/**
 * Created by Константин on 05.01.2017.
 */
public class ChatFilterFrame extends TitledComponentFrame {
    public ChatFilterFrame() {
        super("MT-ChatFilter");
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Chat filter";
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
