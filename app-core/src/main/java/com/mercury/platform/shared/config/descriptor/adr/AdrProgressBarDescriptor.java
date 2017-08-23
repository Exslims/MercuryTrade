package com.mercury.platform.shared.config.descriptor.adr;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdrProgressBarDescriptor extends AdrDurationComponentDescriptor implements Serializable {
    private AdrIconAlignment iconAlignment = AdrIconAlignment.LEFT;
}
