package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.configration.impl.ApplicationConfigurationService;
import com.mercury.platform.ui.components.panel.TransparentPanel;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;

public abstract class ConfigurationPanel extends TransparentPanel implements HasUI{
    public abstract boolean processAndSave();

    public abstract void restore();
}
