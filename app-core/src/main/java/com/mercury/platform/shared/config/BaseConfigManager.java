package com.mercury.platform.shared.config;

import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.configration.impl.*;
import com.mercury.platform.shared.config.configration.impl.adr.AdrConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.entity.adr.AdrProfile;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class BaseConfigManager implements ConfigManager, AsSubscriber {
    private Logger logger = LogManager.getLogger(BaseConfigManager.class.getSimpleName());

    private ConfigurationSource dataSource;
    private JSONHelper jsonHelper;
    private List<ProfileDescriptor> profileDescriptors;
    private FramesConfigurationService framesConfigurationService;
    private PlainConfigurationService<ApplicationDescriptor> applicationConfigurationService;
    private PlainConfigurationService<NotificationDescriptor> notificationConfigurationService;
    private PlainConfigurationService<ScannerDescriptor> scannerConfigurationService;
    private KeyValueConfigurationService<SoundDescriptor,String> soundConfigurationService;
    private ListConfigurationService<AdrProfile> adrGroupConfiguration;

    public BaseConfigManager(ConfigurationSource dataSource){
        this.dataSource = dataSource;
        this.jsonHelper = new JSONHelper(dataSource);
        this.subscribe();
    }
    @Override
    public FramesConfigurationService framesConfiguration() {
        return this.framesConfigurationService;
    }

    @Override
    public PlainConfigurationService<ApplicationDescriptor> applicationConfiguration() {
        return this.applicationConfigurationService;
    }

    @Override
    public PlainConfigurationService<NotificationDescriptor> notificationConfiguration() {
        return this.notificationConfigurationService;
    }

    @Override
    public PlainConfigurationService<ScannerDescriptor> scannerConfiguration() {
        return this.scannerConfigurationService;
    }

    @Override
    public KeyValueConfigurationService<SoundDescriptor,String> soundConfiguration() {
        return this.soundConfigurationService;
    }
    @Override
    public ListConfigurationService<AdrProfile> adrGroupConfiguration() {
        return this.adrGroupConfiguration;
    }


    public void load(){
        try {
            File file = new File(dataSource.getConfigurationFilePath());

            if(!file.exists()){
                file.createNewFile();
            }
            this.profileDescriptors = this.jsonHelper.readArrayData(new TypeToken<List<ProfileDescriptor>>(){});
            if(this.profileDescriptors == null){
                this.profileDescriptors = new ArrayList<>();
                ProfileDescriptor defaultProfile = new ProfileDescriptor();
                defaultProfile.setSelected(true);
                defaultProfile.setProfileName("Profile1");
                this.profileDescriptors.add(defaultProfile);
                this.jsonHelper.writeListObject(this.profileDescriptors,new TypeToken<List<ProfileDescriptor>>(){});
            }

            ProfileDescriptor selectedProfile =  this.profileDescriptors.stream()
                    .filter(ProfileDescriptor::isSelected)
                    .findAny().orElse(null);

            this.framesConfigurationService = new FramesConfigurationServiceImpl(selectedProfile);
            this.soundConfigurationService = new SoundConfigurationService(selectedProfile);
            this.adrGroupConfiguration = new AdrConfigurationService(selectedProfile);
            this.applicationConfigurationService = new ApplicationConfigurationService(selectedProfile);
            this.scannerConfigurationService = new ScannerConfigurationService(selectedProfile);
            this.notificationConfigurationService = new NotificationConfigurationService(selectedProfile);

            this.framesConfigurationService.validate();
            this.soundConfigurationService.validate();
            this.adrGroupConfiguration.validate();
            this.applicationConfigurationService.validate();
            this.notificationConfigurationService.validate();
            this.scannerConfigurationService.validate();

            this.jsonHelper.writeListObject(this.profileDescriptors,new TypeToken<List<ProfileDescriptor>>(){});
        }catch (IOException e) {
            logger.error("Error while processing file:{}",dataSource.getConfigurationFilePath(),e);
        }
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.INSTANCE.saveConfigSubject.subscribe(state -> {
            this.jsonHelper.writeListObject(this.profileDescriptors,new TypeToken<List<ProfileDescriptor>>(){});
        });
    }
}
