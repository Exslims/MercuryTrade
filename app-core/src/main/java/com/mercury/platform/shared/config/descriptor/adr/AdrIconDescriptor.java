package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrIconDescriptor extends AdrComponentDescriptor {
    private String iconPath = "";
    private float duration = 9.0f;
    public AdrIconDescriptor(String iconPath, Point location, Dimension size, float duration){
        super(location,size);
        this.iconPath = iconPath;
        this.duration = duration;
    }
}
