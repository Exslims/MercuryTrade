package com.mercury.platform.shared.config.descriptor.adr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdrComponentWrapper {
    private AdrComponentType type;
    private AdrComponentDescriptor componentDescriptor;
}
