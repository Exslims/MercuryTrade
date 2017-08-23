package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.StashTabDescriptor;

import java.util.ArrayList;
import java.util.List;

public class StashTabConfigurationService extends BaseConfigurationService<List<StashTabDescriptor>> implements ListConfigurationService<StashTabDescriptor> {
    public StashTabConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public List<StashTabDescriptor> getDefault() {
        return new ArrayList<>();
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setStashTabDescriptors(this.getDefault());
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getStashTabDescriptors() == null) {
            this.selectedProfile.setStashTabDescriptors(this.getDefault());
        }
    }

    @Override
    public List<StashTabDescriptor> getEntities() {
        return this.selectedProfile.getStashTabDescriptors();
    }
}
