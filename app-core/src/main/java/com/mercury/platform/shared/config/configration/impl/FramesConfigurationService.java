package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;

import java.awt.*;


public interface FramesConfigurationService extends KeyValueConfigurationService<FrameDescriptor,String> {
    Dimension getMinimumSize(String frameClass);
}
