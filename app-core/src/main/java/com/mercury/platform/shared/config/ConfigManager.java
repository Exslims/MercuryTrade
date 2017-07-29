package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.configration.*;
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
    KeyValueConfigurationService<String,HotKeyDescriptor> hotKeysConfiguration();
    AdrConfigurationService adrConfiguration();
    ListConfigurationService<StashTabDescriptor> stashTabConfiguration();
    IconBundleConfigurationService iconBundleConfiguration();
    List<ProfileDescriptor> profiles();
}
