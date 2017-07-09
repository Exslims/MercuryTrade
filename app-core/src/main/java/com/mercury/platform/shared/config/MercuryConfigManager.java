package com.mercury.platform.shared.config;

import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.configration.*;
import com.mercury.platform.shared.config.configration.impl.*;
import com.mercury.platform.shared.config.configration.impl.adr.AdrConfigurationServiceMock;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.config.descriptor.StashTabDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.shared.config.json.JSONHelper;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class MercuryConfigManager implements ConfigManager, AsSubscriber {
    private Logger logger = LogManager.getLogger(MercuryConfigManager.class.getSimpleName());

    private ConfigurationSource dataSource;
    private JSONHelper jsonHelper;
    private List<ProfileDescriptor> profileDescriptors;
    private ProfileDescriptor selectedProfile;
    private FramesConfigurationService framesConfigurationService;
    private PlainConfigurationService<ApplicationDescriptor> applicationConfigurationService;
    private PlainConfigurationService<NotificationDescriptor> notificationConfigurationService;
    private PlainConfigurationService<ScannerDescriptor> scannerConfigurationService;
    private KeyValueConfigurationService<String,SoundDescriptor> soundConfigurationService;
    private KeyValueConfigurationService<String,Float> scaleConfigurationService;
    private KeyValueConfigurationService<String,HotKeyDescriptor> hotKeyConfigurationService;
    private ListConfigurationService<AdrProfileDescriptor> adrGroupConfiguration;
    private ListConfigurationService<StashTabDescriptor> stashTabConfigurationService;
    private ListConfigurationService<AdrProfileDescriptor> adrConfigurationService;

    private List<BaseConfigurationService> services = new ArrayList<>();

    public MercuryConfigManager(ConfigurationSource dataSource){
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
    public KeyValueConfigurationService<String,SoundDescriptor> soundConfiguration() {
        return this.soundConfigurationService;
    }

    @Override
    public KeyValueConfigurationService<String,Float> scaleConfiguration() {
        return this.scaleConfigurationService;
    }

    @Override
    public ListConfigurationService<AdrProfileDescriptor> adrGroupConfiguration() {
        return this.adrGroupConfiguration;
    }

    @Override
    public ListConfigurationService<StashTabDescriptor> stashTabConfiguration() {
        return this.stashTabConfigurationService;
    }

    @Override
    public ListConfigurationService<AdrProfileDescriptor> adrConfiguration() {
        return this.adrConfigurationService;
    }

    @Override
    public KeyValueConfigurationService<String, HotKeyDescriptor> hotKeysConfiguration() {
        return this.hotKeyConfigurationService;
    }

    @Override
    public List<ProfileDescriptor> profiles() {
        return this.profileDescriptors;
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
                this.selectedProfile = defaultProfile;
                this.profileDescriptors.add(defaultProfile);
                this.jsonHelper.writeListObject(this.profileDescriptors,new TypeToken<List<ProfileDescriptor>>(){});
            }else {
                this.selectedProfile = this.profileDescriptors.stream()
                        .filter(ProfileDescriptor::isSelected)
                        .findAny().orElse(null);
            }

            this.framesConfigurationService = new FramesConfigurationServiceImpl(selectedProfile);
            this.soundConfigurationService = new SoundConfigurationService(selectedProfile);
            this.adrGroupConfiguration = new AdrConfigurationServiceMock(selectedProfile);
            this.applicationConfigurationService = new ApplicationConfigurationService(selectedProfile);
            this.scannerConfigurationService = new ScannerConfigurationService(selectedProfile);
            this.notificationConfigurationService = new NotificationConfigurationService(selectedProfile);
            this.scaleConfigurationService = new ScaleConfigurationService(selectedProfile);
            this.stashTabConfigurationService = new StashTabConfigurationService(selectedProfile);
            this.hotKeyConfigurationService = new HotKeysConfigurationService(selectedProfile);
            this.adrConfigurationService = new AdrConfigurationServiceMock(selectedProfile);

            this.services.add((BaseConfigurationService) this.framesConfigurationService);
            this.services.add((BaseConfigurationService) this.soundConfigurationService);
            this.services.add((BaseConfigurationService) this.adrGroupConfiguration);
            this.services.add((BaseConfigurationService) this.applicationConfigurationService);
            this.services.add((BaseConfigurationService) this.scannerConfigurationService);
            this.services.add((BaseConfigurationService) this.notificationConfigurationService);
            this.services.add((BaseConfigurationService) this.scaleConfigurationService);
            this.services.add((BaseConfigurationService) this.stashTabConfigurationService);
            this.services.add((BaseConfigurationService) this.hotKeyConfigurationService);
            this.services.add((BaseConfigurationService) this.adrConfigurationService);

            this.services.forEach(BaseConfigurationService::validate);


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
        MercuryStoreCore.INSTANCE.toDefaultSubject.subscribe(state -> {
            this.services.forEach(BaseConfigurationService::toDefault);
        });
        MercuryStoreCore.INSTANCE.changeProfileSubject.subscribe(profile -> {
            this.selectedProfile.setSelected(false);
            this.selectedProfile = profile;
            profile.setSelected(true);
            this.services.forEach(service -> {
                service.setSelectedProfile(profile);
                service.validate();
            });
            this.jsonHelper.writeListObject(this.profileDescriptors,new TypeToken<List<ProfileDescriptor>>(){});
        });
    }
}
