package com.mercury.platform.shared.entity.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemTradeNotificationDescriptor extends TradeNotificationDescriptor {
    private String itemName;
    private String tabName;
    private int left;
    private int top;
    private List<String> itemsWanted;
    private List<String> itemsOffered;
}
