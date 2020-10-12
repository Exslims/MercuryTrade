package com.mercury.platform.ui.frame.movable;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.entity.message.CurrencyTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import net.jodah.expiringmap.ExpiringMap;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class NotificationPreProcessor implements AsSubscriber {
    private List<NotificationDescriptor> currentMessages;
    private Map<String, String> expiresMessages;

    public NotificationPreProcessor() {
        this.currentMessages = new ArrayList<>();
        this.expiresMessages = ExpiringMap.builder()
                .expiration(1, TimeUnit.HOURS)
                .build();
        this.subscribe();
    }

    public boolean isAllowed(NotificationDescriptor descriptor) {
        String descriptorData = this.getDescriptorData(descriptor);
        if (expiresMessages.containsValue(descriptorData)) {
            return false;
        }
        if (!this.currentMessages.contains(descriptor)) {
            this.currentMessages.add(descriptor);
            return true;
        }
        return false;
    }

    public boolean isDuplicate(NotificationDescriptor descriptor) {
        String descriptorData = this.getDescriptorData(descriptor);
        return this.currentMessages.stream().filter(it ->
                this.getDescriptorData(it).equals(descriptorData))
                .findAny()
                .orElse(null) != null;
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.expiredNotificationSubject.subscribe(notificationDescriptor -> {
            this.expiresMessages.put(UUID.randomUUID().toString(), this.getDescriptorData(notificationDescriptor));
        });
        MercuryStoreCore.removeNotificationSubject.subscribe(notification -> {
            this.currentMessages.remove(notification);
        });
    }

    public String getDescriptorData(NotificationDescriptor notificationDescriptor) {
        switch (notificationDescriptor.getType()) {
            case INC_ITEM_MESSAGE: {
                ItemTradeNotificationDescriptor descriptor = (ItemTradeNotificationDescriptor) notificationDescriptor;
                if (Objects.nonNull(descriptor.getTabName())) {
                    return descriptor.getItemName() + ":" + descriptor.getTabName() + ":" + descriptor.getLeft() + ":" + descriptor.getTop();
                } else {
                    return descriptor.getItemName();
                }
            }
            case INC_CURRENCY_MESSAGE: {
                CurrencyTradeNotificationDescriptor descriptor = (CurrencyTradeNotificationDescriptor) notificationDescriptor;
                if (descriptor.getCurrForSaleTitle() != null) {
                    return descriptor.getCurrForSaleTitle();
                }
            }
        }
        return UUID.randomUUID().toString();
    }
}
