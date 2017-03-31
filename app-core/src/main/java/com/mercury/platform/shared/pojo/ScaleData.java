package com.mercury.platform.shared.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScaleData {
    private float notificationScale;
    private float taskBarScale;
    private float itemCellScale;
}
