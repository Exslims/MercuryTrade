package com.mercury.platform.shared.config.descriptor.adr;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdrComponentDescriptor implements Serializable {
    private String componentId = UUID.randomUUID().toString();
    private String title = "component";
    private AdrComponentType type;
    private Point location = new Point(new Random().nextInt(600), new Random().nextInt(600));
    private Dimension size = new Dimension(64, 64);
    private boolean visible = true;
    private float scale = 1f;
    private float opacity = 1f;
    private AdrComponentOrientation orientation = AdrComponentOrientation.HORIZONTAL;
}
