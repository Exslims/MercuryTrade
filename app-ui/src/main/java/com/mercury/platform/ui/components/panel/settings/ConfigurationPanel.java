package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.panel.TransparentPanel;
import com.mercury.platform.ui.components.panel.misc.ViewInit;

public abstract class ConfigurationPanel extends TransparentPanel implements ViewInit {
    public abstract boolean processAndSave();

    public abstract void restore();
}
