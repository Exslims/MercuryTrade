package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.configration.impl.FramesConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.entity.adr.AdrProfile;


public interface ConfigManager {
    FramesConfigurationService framesConfiguration();
    PlainConfigurationService<ApplicationDescriptor> applicationConfiguration();
    PlainConfigurationService<NotificationDescriptor> notificationConfiguration();
    PlainConfigurationService<ScannerDescriptor> scannerConfiguration();
    KeyValueConfigurationService<SoundDescriptor,String> soundConfiguration();
    ListConfigurationService<AdrProfile> adrGroupConfiguration();
}
