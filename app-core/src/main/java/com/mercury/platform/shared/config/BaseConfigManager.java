package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.configration.impl.FramesConfigurationService;
import com.mercury.platform.shared.config.configration.impl.SoundConfigurationService;
import com.mercury.platform.shared.config.configration.impl.adr.AdrConfigurationService;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.entity.SoundDescriptor;
import com.mercury.platform.shared.entity.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.entity.adr.AdrProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;


public class BaseConfigManager implements ConfigManager {
    private Logger logger = LogManager.getLogger(BaseConfigManager.class.getSimpleName());

    private ConfigurationSource dataSource;
    private KeyValueConfigurationService<FrameSettings,String> framesConfigurationService;
    private KeyValueConfigurationService<SoundDescriptor,String> soundConfigurationService;
    private ListConfigurationService<AdrProfile> adrGroupConfiguration;

    public BaseConfigManager(ConfigurationSource dataSource){
        this.dataSource = dataSource;

        this.framesConfigurationService = new FramesConfigurationService(dataSource);
        this.soundConfigurationService = new SoundConfigurationService(dataSource);
        this.adrGroupConfiguration = new AdrConfigurationService(dataSource);
    }
    @Override
    public KeyValueConfigurationService<FrameSettings,String> framesConfiguration() {
        return framesConfigurationService;
    }
    @Override
    public KeyValueConfigurationService<SoundDescriptor,String> soundConfiguration() {
        return soundConfigurationService;
    }
    @Override
    public ListConfigurationService<AdrProfile> adrGroupConfiguration() {
        return adrGroupConfiguration;
    }

    public void load(){
        try {
            File file = new File(dataSource.getConfigurationFilePath());

            if(!file.exists()){
                file.createNewFile();
            }
            this.framesConfigurationService.load();
            this.soundConfigurationService.load();
            this.adrGroupConfiguration.load();
        }catch (IOException e) {
            logger.error("Error while processing file:{}",dataSource.getConfigurationFilePath(),e);
        }
    }
}
