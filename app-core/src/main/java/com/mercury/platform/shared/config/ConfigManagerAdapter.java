package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.service.ConfigurationService;
import lombok.Setter;


public class ConfigManagerAdapter implements ConfigManager {
    @Setter
    private ConfigurationService framesConfigurationService;
    @Setter
    private ConfigurationService soundConfigurationService;
    @Override
    public ConfigurationService framesConfigurationService() {
        return framesConfigurationService;
    }
    @Override
    public ConfigurationService soundConfigurationService() {
        return soundConfigurationService;
    }

    /**
     * Initializing all services.
     */
    public void load(){
        this.framesConfigurationService.load();
        this.soundConfigurationService.load();
    }
}
