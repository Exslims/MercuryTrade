package com.mercury.platform.shared.entity.atr;

import com.mercury.platform.shared.entity.FrameSettings;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AtrGroupSettings {
    private FrameSettings frameSettings;
    private float opacity;
    private float scale;
}
