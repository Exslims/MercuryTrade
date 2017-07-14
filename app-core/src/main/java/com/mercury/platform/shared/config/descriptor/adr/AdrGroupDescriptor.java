package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrGroupDescriptor extends AdrComponentDescriptor {
    private List<AdrComponentDescriptor> cells;
    private AdrGroupContentType contentType;
    private AdrGroupType groupType;
}
