package com.mercury.platform.shared.config;

import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.configration.*;
import com.mercury.platform.shared.config.configration.impl.*;
import com.mercury.platform.shared.config.configration.impl.adr.AdrConfigurationServiceMock;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.config.json.JSONHelper;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MercuryConfigManager implements ConfigManager, AsSubscriber {
    private Logger logger = LogManager.getLogger(MercuryConfigManager.class.getSimpleName());

    private ConfigurationSource dataSource;
    private JSONHelper jsonHelper;
    private List<ProfileDescriptor> profileDescriptors;
    private ProfileDescriptor selectedProfile;
    private FramesConfigurationService framesConfigurationService;
    private PlainConfigurationService<ApplicationDescriptor> applicationConfigurationService;
    private PlainConfigurationService<NotificationSettingsDescriptor> notificationConfigurationService;
    private PlainConfigurationService<TaskBarDescriptor> taskBarConfigurationService;
    private PlainConfigurationService<ScannerDescriptor> scannerConfigurationService;
    private KeyValueConfigurationService<String, SoundDescriptor> soundConfigurationService;
    private KeyValueConfigurationService<String, Float> scaleConfigurationService;
    private PlainConfigurationService<HotKeysSettingsDescriptor> hotKeyConfigurationService;
    private ListConfigurationService<StashTabDescriptor> stashTabConfigurationService;
    private IconBundleConfigurationService iconBundleConfigurationService;
    private AdrConfigurationService adrConfigurationService;

    private List<BaseConfigurationService> services = new ArrayList<>();

    public MercuryConfigManager(ConfigurationSource dataSource) {
        this.dataSource = dataSource;
        this.jsonHelper = new JSONHelper(dataSource.getConfigurationFilePath());
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
    public PlainConfigurationService<NotificationSettingsDescriptor> notificationConfiguration() {
        return this.notificationConfigurationService;
    }

    @Override
    public PlainConfigurationService<TaskBarDescriptor> taskBarConfiguration() {
        return this.taskBarConfigurationService;
    }

    @Override
    public PlainConfigurationService<ScannerDescriptor> scannerConfiguration() {
        return this.scannerConfigurationService;
    }

    @Override
    public KeyValueConfigurationService<String, SoundDescriptor> soundConfiguration() {
        return this.soundConfigurationService;
    }

    @Override
    public KeyValueConfigurationService<String, Float> scaleConfiguration() {
        return this.scaleConfigurationService;
    }

    @Override
    public AdrConfigurationService adrConfiguration() {
        return this.adrConfigurationService;
    }

    @Override
    public ListConfigurationService<StashTabDescriptor> stashTabConfiguration() {
        return this.stashTabConfigurationService;
    }

    @Override
    public IconBundleConfigurationService iconBundleConfiguration() {
        return this.iconBundleConfigurationService;
    }

    @Override
    public PlainConfigurationService<HotKeysSettingsDescriptor> hotKeysConfiguration() {
        return this.hotKeyConfigurationService;
    }

    @Override
    public List<ProfileDescriptor> profiles() {
        return this.profileDescriptors;
    }


    public void load() {
        try {
            File configFile = new File(dataSource.getConfigurationFilePath());
            File configFolder = new File(dataSource.getConfigurationPath());
            File iconFolder = new File(dataSource.getConfigurationPath() + "\\icons");
            if (!configFolder.exists() || !configFile.exists() || !iconFolder.exists()) {
                new File(dataSource.getConfigurationPath() + "\\temp").mkdir();
                new File(dataSource.getConfigurationPath() + "\\icons").mkdir();
                new File(dataSource.getConfigurationFilePath()).createNewFile();

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream resourceAsStream = classLoader.getResourceAsStream("app/local-updater.jar");
                File dest = new File(dataSource.getConfigurationPath() + "\\local-updater.jar");
                FileUtils.copyInputStreamToFile(resourceAsStream, dest);
            }
            this.profileDescriptors = this.jsonHelper.readArrayData(new TypeToken<List<ProfileDescriptor>>() {
            });
            if (this.profileDescriptors == null) {
                this.profileDescriptors = new ArrayList<>();
                ProfileDescriptor defaultProfile = this.getDefaultProfile();
                this.selectedProfile = defaultProfile;
                this.profileDescriptors.add(defaultProfile);
                this.jsonHelper.writeListObject(this.profileDescriptors, new TypeToken<List<ProfileDescriptor>>() {
                });
            } else {
                this.selectedProfile = this.profileDescriptors.stream()
                        .filter(ProfileDescriptor::isSelected)
                        .findAny().orElse(null);
                if (this.selectedProfile == null) {
                    ProfileDescriptor defaultProfile = this.getDefaultProfile();
                    this.selectedProfile = defaultProfile;
                    this.profileDescriptors.add(defaultProfile);
                    this.jsonHelper.writeListObject(this.profileDescriptors, new TypeToken<List<ProfileDescriptor>>() {
                    });
                }
            }

            this.framesConfigurationService = new FramesConfigurationServiceImpl(selectedProfile);
            this.soundConfigurationService = new SoundConfigurationService(selectedProfile);
            this.applicationConfigurationService = new ApplicationConfigurationService(selectedProfile);
            this.taskBarConfigurationService = new TaskBarConfigurationService(selectedProfile);
            this.scannerConfigurationService = new ScannerConfigurationService(selectedProfile);
            this.notificationConfigurationService = new NotificationConfigurationService(selectedProfile);
            this.scaleConfigurationService = new ScaleConfigurationService(selectedProfile);
            this.stashTabConfigurationService = new StashTabConfigurationService(selectedProfile);
            this.hotKeyConfigurationService = new HotKeyConfigurationService(selectedProfile);
            this.adrConfigurationService = new AdrConfigurationServiceMock(selectedProfile);
            this.iconBundleConfigurationService = new IconBundleConfigurationServiceImpl(selectedProfile);

            this.services.add((BaseConfigurationService) this.framesConfigurationService);
            this.services.add((BaseConfigurationService) this.soundConfigurationService);
            this.services.add((BaseConfigurationService) this.applicationConfigurationService);
            this.services.add((BaseConfigurationService) this.scannerConfigurationService);
            this.services.add((BaseConfigurationService) this.taskBarConfigurationService);
            this.services.add((BaseConfigurationService) this.notificationConfigurationService);
            this.services.add((BaseConfigurationService) this.scaleConfigurationService);
            this.services.add((BaseConfigurationService) this.stashTabConfigurationService);
            this.services.add((BaseConfigurationService) this.hotKeyConfigurationService);
            this.services.add((BaseConfigurationService) this.adrConfigurationService);
            this.services.add((BaseConfigurationService) this.iconBundleConfigurationService);

            this.services.forEach(BaseConfigurationService::validate);


            this.jsonHelper.writeListObject(this.profileDescriptors, new TypeToken<List<ProfileDescriptor>>() {
            });
        } catch (IOException e) {
            logger.error("Error while processing file:{}", dataSource.getConfigurationPath(), e);
        }
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.saveConfigSubject.subscribe(state -> {
            this.jsonHelper.writeListObject(this.profileDescriptors, new TypeToken<List<ProfileDescriptor>>() {
            });
        });
        MercuryStoreCore.toDefaultSubject.subscribe(state -> {
            this.services.forEach(BaseConfigurationService::toDefault);
        });
        MercuryStoreCore.changeProfileSubject.subscribe(profile -> {
            this.selectedProfile.setSelected(false);
            this.selectedProfile = profile;
            profile.setSelected(true);
            this.services.forEach(service -> {
                service.setSelectedProfile(profile);
                service.validate();
            });
            this.jsonHelper.writeListObject(this.profileDescriptors, new TypeToken<List<ProfileDescriptor>>() {
            });
        });
    }

    private ProfileDescriptor getDefaultProfile() {
        ProfileDescriptor defaultProfile = new ProfileDescriptor();
        defaultProfile.setSelected(true);
        defaultProfile.setName("Profile1");
        return defaultProfile;
    }
}
