package com.mercury.platform.ui.adr.routing;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdrComponentDefinition {
    private AdrComponentDescriptor descriptor;
    private List<AdrComponentDescriptor> descriptors;
    private AdrComponentOperations operations;
    private AdrComponentDescriptor parent;
    private boolean fromGroup;

    public AdrComponentDefinition(AdrComponentDescriptor descriptor, AdrComponentOperations operations, boolean fromGroup) {
        this.descriptor = descriptor;
        this.operations = operations;
        this.fromGroup = fromGroup;
    }

    public AdrComponentDefinition(AdrComponentDescriptor descriptor, AdrComponentOperations operations, AdrComponentDescriptor parent) {
        this.descriptor = descriptor;
        this.operations = operations;
        this.parent = parent;
    }
}
