package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrIconDescriptor extends AdrDurationComponentDescriptor {
    private AdrIconType iconType;
}
