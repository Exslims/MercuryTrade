package com.mercury.platform.shared.config;

import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.config.descriptor.SoundDescriptor;
import com.mercury.platform.shared.entity.adr.AdrProfile;


public interface ConfigManager {
    KeyValueConfigurationService<FrameDescriptor,String> framesConfiguration();
    KeyValueConfigurationService<SoundDescriptor,String> soundConfiguration();
    ListConfigurationService<AdrProfile> adrGroupConfiguration();
}
