package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrTrackerGroupDescriptor extends AdrComponentDescriptor implements Serializable {
    private List<AdrComponentDescriptor> cells = new ArrayList<>();
    private AdrTrackerGroupContentType contentType;
    private AdrTrackerGroupType groupType = AdrTrackerGroupType.STATIC;
    private int vGap = 1;
    private int hGap = 1;
}
