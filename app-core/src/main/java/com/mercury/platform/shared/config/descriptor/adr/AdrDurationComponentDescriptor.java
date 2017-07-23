package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrDurationComponentDescriptor extends AdrComponentDescriptor{
    private String iconPath;
    private float duration;
    private float fontSize = 28f;
    private boolean textEnable = true;
    private boolean iconEnable = true;
    private boolean animationEnable = true;
    private boolean invert;
    private String textFormat;
    private Color lowValueTextColor;
    private Color mediumValueTextColor;
    private Color defaultValueTextColor;
}
