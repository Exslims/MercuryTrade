package com.mercury.platform.shared.entity.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TradeNotificationDescriptor extends NotificationDescriptor {
    private String offer;
    private Double curCount;
    private String currency;
    private String league;
}
