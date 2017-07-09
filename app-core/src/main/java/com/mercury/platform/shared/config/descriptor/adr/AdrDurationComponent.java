package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrDurationComponent extends AdrComponentDescriptor{
    private String iconPath;
    private float duration;
    private boolean textEnable = true;
    private boolean iconEnable = true;
    private boolean animationEnable = true;
    private boolean invert;
}
