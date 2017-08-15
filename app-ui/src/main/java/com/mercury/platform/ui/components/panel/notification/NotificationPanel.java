package com.mercury.platform.ui.components.panel.notification;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewDestroy;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

public abstract class NotificationPanel<T,C> extends JPanel implements AsSubscriber, ViewInit,ViewDestroy {
    @Setter
    @Getter
    protected T data;
    @Setter
    protected C controller;
    @Setter
    protected ComponentsFactory componentsFactory;
}
