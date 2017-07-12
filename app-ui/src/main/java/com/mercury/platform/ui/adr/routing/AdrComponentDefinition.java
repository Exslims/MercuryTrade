package com.mercury.platform.ui.adr.routing;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdrComponentDefinition {
    private AdrComponentDescriptor descriptor;
    private AdrComponentOperations operations;
    private boolean fromGroup;
}
