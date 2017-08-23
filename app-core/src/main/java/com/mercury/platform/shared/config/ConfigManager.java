package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.configration.*;
import com.mercury.platform.shared.config.descriptor.*;

import java.util.List;


public interface ConfigManager {
    FramesConfigurationService framesConfiguration();

    PlainConfigurationService<ApplicationDescriptor> applicationConfiguration();

    PlainConfigurationService<NotificationSettingsDescriptor> notificationConfiguration();

    PlainConfigurationService<TaskBarDescriptor> taskBarConfiguration();

    PlainConfigurationService<ScannerDescriptor> scannerConfiguration();

    KeyValueConfigurationService<String, SoundDescriptor> soundConfiguration();

    KeyValueConfigurationService<String, Float> scaleConfiguration();

    PlainConfigurationService<HotKeysSettingsDescriptor> hotKeysConfiguration();

    AdrConfigurationService adrConfiguration();

    ListConfigurationService<StashTabDescriptor> stashTabConfiguration();

    IconBundleConfigurationService iconBundleConfiguration();

    List<ProfileDescriptor> profiles();
}
