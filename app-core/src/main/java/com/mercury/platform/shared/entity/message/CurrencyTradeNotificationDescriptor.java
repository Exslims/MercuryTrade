package com.mercury.platform.shared.entity.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CurrencyTradeNotificationDescriptor extends TradeNotificationDescriptor {
    private Double currForSaleCount;
    private String currForSaleTitle;
    private List<String> items = new ArrayList<>();
}
