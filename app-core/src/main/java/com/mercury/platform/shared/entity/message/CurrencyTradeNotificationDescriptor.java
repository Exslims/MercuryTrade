package com.mercury.platform.shared.entity.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CurrencyTradeNotificationDescriptor extends TradeNotificationDescriptor {
    private Double currForSaleCount;
    private String currForSaleTitle;
}
