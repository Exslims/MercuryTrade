package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;

import java.util.List;

@Data
public class AdrProfileDescriptor {
    private String profileName = "default";
    private List<AdrComponentWrapper> contents;
}
