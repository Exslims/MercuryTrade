package com.mercury.platform.shared.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScaleData {
    private float notificationScale;
    private float taskBarScale;
    private float itemCellScale;
}
