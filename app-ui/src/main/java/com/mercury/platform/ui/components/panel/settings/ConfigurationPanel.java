package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.panel.TransparentPanel;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;

/**
 * Created by Константин on 05.01.2017.
 */
public abstract class ConfigurationPanel extends TransparentPanel{
    protected ConfigurationPanel() {
        this.setBorder(BorderFactory.createMatteBorder(1,0,0,0, AppThemeColor.BORDER));
    }

    public abstract void processAndSave();
}
