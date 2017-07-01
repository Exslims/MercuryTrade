package com.mercury.platform.shared.config.descriptor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StashTabDescriptor {
    private String title;
    private boolean isQuad;
    private boolean undefined;
}
