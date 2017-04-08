package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.service.ConfigurationService;
import com.mercury.platform.shared.config.service.FramesConfigurationService;
import com.mercury.platform.shared.config.service.SoundConfigurationService;


public interface ConfigManager {
    ConfigurationService framesConfigurationService();
    ConfigurationService soundConfigurationService();
}
