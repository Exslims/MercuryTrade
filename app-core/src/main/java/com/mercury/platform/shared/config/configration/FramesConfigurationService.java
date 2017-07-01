package com.mercury.platform.shared.config.configration;

import com.mercury.platform.shared.config.descriptor.FrameDescriptor;

import java.awt.*;


public interface FramesConfigurationService extends KeyValueConfigurationService<FrameDescriptor,String> {
    Dimension getMinimumSize(String frameClass);
}
