package com.mercury.platform.shared.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemMessage extends Message {
    private String itemName;
    private String tabName;
    private int left;
    private int top;
}
