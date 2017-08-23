package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.SoundDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;

import java.util.HashMap;
import java.util.Map;


public class SoundConfigurationService extends BaseConfigurationService<Map<String, SoundDescriptor>> implements KeyValueConfigurationService<String, SoundDescriptor> {
    public SoundConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getSoundDescriptorMap() == null) {
            this.selectedProfile.setSoundDescriptorMap(this.getDefault());
        }
    }

    @Override
    public SoundDescriptor get(String key) {
        return this.selectedProfile.getSoundDescriptorMap().computeIfAbsent(key, k -> {
            this.selectedProfile.getSoundDescriptorMap().put(key, this.getDefault().get(key));
            MercuryStoreCore.saveConfigSubject.onNext(true);
            return this.getDefault().get(key);
        });
    }

    @Override
    public Map<String, SoundDescriptor> getMap() {
        return this.selectedProfile.getSoundDescriptorMap();
    }

    @Override
    public void set(Map<String, SoundDescriptor> map) {
        this.selectedProfile.setSoundDescriptorMap(map);
    }

    @Override
    public Map<String, SoundDescriptor> getDefault() {
        Map<String, SoundDescriptor> defaultSettings = new HashMap<>();
        defaultSettings.put("notification", new SoundDescriptor("app/notification.wav", 0f));
        defaultSettings.put("chat_scanner", new SoundDescriptor("app/chat-filter.wav", 0f));
        defaultSettings.put("clicks", new SoundDescriptor("default", 0f));
        defaultSettings.put("update", new SoundDescriptor("default", 0f));
        return defaultSettings;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setSoundDescriptorMap(this.getDefault());
    }
}
