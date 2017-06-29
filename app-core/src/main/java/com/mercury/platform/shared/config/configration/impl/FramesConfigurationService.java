package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;

import java.util.Map;


public class FramesConfigurationService extends BaseConfigurationService implements KeyValueConfigurationService<FrameDescriptor,String> {
    public FramesConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public void validate() {
        if(this.selectedProfile.getFrameDescriptorMap() == null) {
            this.selectedProfile.setFrameDescriptorMap(this.getDefault());
        }
    }
    @Override
    public FrameDescriptor get(String key) {
        return this.selectedProfile.getFrameDescriptorMap().computeIfAbsent(key, k -> this.getDefault().get(key));
    }

    @Override
    public Map<String, FrameDescriptor> getMap() {
        return this.selectedProfile.getFrameDescriptorMap();
    }

    @Override
    public Map<String, FrameDescriptor> getDefault() {
        return null;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setFrameDescriptorMap(this.getDefault());
    }
}
