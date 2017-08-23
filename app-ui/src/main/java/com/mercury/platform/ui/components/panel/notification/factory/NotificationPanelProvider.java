package com.mercury.platform.ui.components.panel.notification.factory;


import com.mercury.platform.shared.entity.message.NotificationType;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.notification.NotificationPanel;

public abstract class NotificationPanelProvider<T, C> {
    protected T data;
    protected C controller;
    protected ComponentsFactory componentsFactory;

    public abstract boolean isSuitable(NotificationType type);

    public NotificationPanelProvider<T, C> setController(C controller) {
        this.controller = controller;
        return this;
    }

    public NotificationPanelProvider<T, C> setData(T data) {
        this.data = data;
        return this;
    }

    public NotificationPanelProvider<T, C> setComponentsFactory(ComponentsFactory factory) {
        this.componentsFactory = factory;
        return this;
    }

    public NotificationPanel<T, C> build() {
        NotificationPanel<T, C> panel = this.getPanel();
        panel.setData(this.data);
        if (this.controller != null) {
            panel.setController(this.controller);
        }
        panel.setComponentsFactory(this.componentsFactory);
        panel.subscribe();
        panel.onViewInit();
        this.componentsFactory = null;
        this.controller = null;
        return panel;
    }

    protected abstract NotificationPanel<T, C> getPanel();
}
