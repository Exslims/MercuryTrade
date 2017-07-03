package com.mercury.platform.shared.entity.adr;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import lombok.Data;

import java.util.List;

@Data
public class AdrProfile {
    private String profileName = "default";
    private List<AdrComponentDescriptor> contents;
}
