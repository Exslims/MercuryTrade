package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.service.ConfigurationService;
import com.mercury.platform.shared.config.service.FramesConfigurationService;
import com.mercury.platform.shared.config.service.SoundConfigurationService;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.entity.SoundDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;


public class BaseConfigManager implements ConfigManager {
    private Logger logger = LogManager.getLogger(BaseConfigManager.class.getSimpleName());

    private DataSource dataSource;
    private ConfigurationService<FrameSettings,String> framesConfigurationService;
    private ConfigurationService<SoundDescriptor,String> soundConfigurationService;

    public BaseConfigManager(DataSource dataSource){
        this.dataSource = dataSource;

        this.framesConfigurationService = new FramesConfigurationService(dataSource);
        this.soundConfigurationService = new SoundConfigurationService(dataSource);
    }
    @Override
    public ConfigurationService<FrameSettings,String> framesConfiguration() {
        return framesConfigurationService;
    }
    @Override
    public ConfigurationService<SoundDescriptor,String> soundConfiguration() {
        return soundConfigurationService;
    }
    public void load(){
        try {
            File file = new File(dataSource.getConfigurationFilePath());

            if(!file.exists()){
                file.createNewFile();
            }
            this.framesConfigurationService.load();
            this.soundConfigurationService.load();
        }catch (IOException e) {
            logger.error("Error while processing file:{}",dataSource.getConfigurationFilePath(),e);
        }
    }
}
