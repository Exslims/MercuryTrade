package com.mercury.platform.shared.config.descriptor.adr;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdrComponentDescriptor {
    private int id;
    private String title = "default";
    private Point location;
    private Dimension size;
    private boolean visible = true;
    private float scale = 1f;
    private float opacity = 1f;
    public AdrComponentDescriptor(Point location, Dimension size){
        this.location = location;
        this.size = size;
    }
}
