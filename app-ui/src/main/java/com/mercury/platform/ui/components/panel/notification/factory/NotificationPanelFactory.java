package com.mercury.platform.ui.components.panel.notification.factory;


import com.mercury.platform.shared.entity.message.NotificationType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class NotificationPanelFactory {
    private List<NotificationPanelProvider> providers = new ArrayList<>();

    public NotificationPanelFactory() {
        this.providers.add(new ItemIncPanelProvider());
        this.providers.add(new CurrencyIncPanelProvider());
        this.providers.add(new ItemOutPanelProvider());
        this.providers.add(new CurrencyOutPanelProvider());
        this.providers.add(new ScannerPanelProvider());
        this.providers.add(new HistoryPanelProvider());
    }

    public NotificationPanelProvider getProviderFor(NotificationType type) {
        NotificationPanelProvider provider = this.providers.stream()
                .filter(it -> it.isSuitable(type))
                .findAny().orElse(null);
        if (provider != null) {
            return provider;
        } else {
            throw new NoSuchElementException("Notification panel provider for <" + type + "> doesn't exist.");
        }
    }
}
