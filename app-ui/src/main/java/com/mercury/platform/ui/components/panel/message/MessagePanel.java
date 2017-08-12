package com.mercury.platform.ui.components.panel.message;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.HasHotkey;

import javax.swing.*;

public abstract class MessagePanel extends JPanel implements AsSubscriber, HasUI {
    protected ComponentsFactory componentsFactory;
    protected PlainConfigurationService<NotificationDescriptor> notificationService;
}
