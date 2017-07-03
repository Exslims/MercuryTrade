package com.mercury.platform.shared.config.descriptor.adr;


import lombok.Data;

@Data
public abstract class AdrComponentDescriptor {
    private int id;
    private String title = "default";
    private boolean visible = true;
    private float scale = 1f;
    private float opacity = 1f;
}
