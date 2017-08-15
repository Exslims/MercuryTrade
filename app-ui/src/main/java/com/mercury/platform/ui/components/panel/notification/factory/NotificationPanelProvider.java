package com.mercury.platform.ui.components.panel.notification.factory;


import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;

public abstract class NotificationPanelProvider<T extends NotificationDescriptor,C> {
    protected T data;
    protected C controller;
    protected ComponentsFactory componentsFactory;

    public abstract boolean isSuitable(NotificationType type);
    public NotificationPanelProvider<T,C> setController(C controller) {
        this.controller = controller;
        return this;
    }
    public NotificationPanelProvider<T,C> setData(T data) {
        this.data = data;
        return this;
    }
    public NotificationPanelProvider<T,C> setComponentsFactory(ComponentsFactory factory) {
        this.componentsFactory = factory;
        return this;
    }

    public abstract NotificationPanel<T,C> build();
}
