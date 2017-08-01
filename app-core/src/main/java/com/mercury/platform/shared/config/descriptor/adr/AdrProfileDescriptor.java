package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdrProfileDescriptor {
    private String profileName = "Default ";
    private List<AdrComponentDescriptor> contents = new ArrayList<>();
    private boolean selected;
}
