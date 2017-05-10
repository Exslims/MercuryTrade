package com.mercury.platform.shared.entity.adr;

import lombok.Data;

import java.util.List;

@Data
public class AdrProfile {
    private String profileName = "default";
    private List<AdrComponentDescriptor> contents;
}
