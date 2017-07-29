package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrDurationComponentDescriptor extends AdrComponentDescriptor implements Serializable {
    private String iconPath;
    private Double duration;
    private int fontSize = 28;
    private boolean textEnable = true;
    private boolean iconEnable = true;
    private boolean animationEnable = true;
    private boolean invert;
    private String textFormat;
    private Color lowValueTextColor;
    private Color mediumValueTextColor;
    private Color defaultValueTextColor;
    private Color borderColor;
    private Double lowValueTextThreshold;
    private Double mediumValueTextThreshold;
    private Double defaultValueTextThreshold;
    private int thickness = 1;
    private Insets insets = new Insets(0,0,0,0);
    private boolean bindToTextColor;
}
