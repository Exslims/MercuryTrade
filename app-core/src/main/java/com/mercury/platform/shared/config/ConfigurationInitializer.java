package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.service.FramesConfigurationService;
import com.mercury.platform.shared.config.service.SoundConfigurationService;


public class ConfigurationInitializer {
    public static ConfigManager createFrom(DataSource dataSource){
        ConfigManagerAdapter configManagerAdapter = new ConfigManagerAdapter();
        configManagerAdapter.setFramesConfigurationService(
                new FramesConfigurationService(dataSource)
        );
        configManagerAdapter.setSoundConfigurationService(
                new SoundConfigurationService(dataSource)
        );
        configManagerAdapter.load();
        return configManagerAdapter;
    }
}
