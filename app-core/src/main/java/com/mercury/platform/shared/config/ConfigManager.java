package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.entity.SoundDescriptor;
import com.mercury.platform.shared.entity.atr.AtrGroupSettings;


public interface ConfigManager {
    KeyValueConfigurationService<FrameSettings,String> framesConfiguration();
    KeyValueConfigurationService<SoundDescriptor,String> soundConfiguration();
    ListConfigurationService<AtrGroupSettings> atrGroupConfiguration();
}
