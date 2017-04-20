package com.mercury.platform.shared.entity.adr;

import com.mercury.platform.shared.entity.FrameSettings;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdrGroupSettings {
    private FrameSettings frameSettings;
    private float opacity;
    private float scale;
}
