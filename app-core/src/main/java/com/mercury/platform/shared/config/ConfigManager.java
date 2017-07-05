package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.configration.FramesConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.config.descriptor.StashTabDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;

import java.util.List;


public interface ConfigManager {
    FramesConfigurationService framesConfiguration();
    PlainConfigurationService<ApplicationDescriptor> applicationConfiguration();
    PlainConfigurationService<NotificationDescriptor> notificationConfiguration();
    PlainConfigurationService<ScannerDescriptor> scannerConfiguration();
    KeyValueConfigurationService<String,SoundDescriptor> soundConfiguration();
    KeyValueConfigurationService<String,Float> scaleConfiguration();
    ListConfigurationService<AdrProfileDescriptor> adrGroupConfiguration();
    ListConfigurationService<StashTabDescriptor> stashTabConfiguration();
    List<ProfileDescriptor> profiles();
}
