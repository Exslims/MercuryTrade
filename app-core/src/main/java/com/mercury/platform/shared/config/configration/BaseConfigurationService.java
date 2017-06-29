package com.mercury.platform.shared.config.configration;

import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import lombok.Getter;
import lombok.Setter;


public abstract class BaseConfigurationService {
    @Getter @Setter
    protected ProfileDescriptor selectedProfile;
    public BaseConfigurationService(ProfileDescriptor selectedProfile){
        this.selectedProfile = selectedProfile;
    }
}
