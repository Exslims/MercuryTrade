package com.mercury.platform.shared.entity.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemTradeNotificationDescriptor extends TradeNotificationDescriptor {
    private String itemName;
    private String tabName;
    private int left;
    private int top;
}
