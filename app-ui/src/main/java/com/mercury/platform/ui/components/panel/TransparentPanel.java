package com.mercury.platform.ui.components.panel;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 05.01.2017.
 */
public abstract class TransparentPanel extends JPanel {
    protected ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    public TransparentPanel() {
        this.setLayout(getPanelLayout());
        this.setBackground(AppThemeColor.TRANSPARENT);
    }
    protected abstract LayoutManager getPanelLayout();
}
