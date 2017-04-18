package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.configration.ConfigurationService;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.entity.SoundDescriptor;


public interface ConfigManager {
    ConfigurationService<FrameSettings,String> framesConfiguration();
    ConfigurationService<SoundDescriptor,String> soundConfiguration();
}
