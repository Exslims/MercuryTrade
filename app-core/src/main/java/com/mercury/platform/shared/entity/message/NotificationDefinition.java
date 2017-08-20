package com.mercury.platform.shared.entity.message;

import lombok.Data;

@Data
public class NotificationDefinition<T> {
    private T payload;
    private NotificationType type;
}
