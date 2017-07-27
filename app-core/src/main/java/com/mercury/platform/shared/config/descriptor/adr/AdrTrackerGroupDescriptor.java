package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrTrackerGroupDescriptor extends AdrComponentDescriptor {
    private List<AdrComponentDescriptor> cells;
    private AdrTrackerGroupContentType contentType;
    private AdrTrackerGroupType groupType;
    private int vGap;
    private int hGap;
}
