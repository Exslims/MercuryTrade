package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.core.misc.WhisperNotifierStatus;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ApplicationDescriptor;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;


public class ApplicationConfigurationService extends BaseConfigurationService<ApplicationDescriptor> implements PlainConfigurationService<ApplicationDescriptor> {
    public ApplicationConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getApplicationDescriptor() == null) {
            this.selectedProfile.setApplicationDescriptor(this.getDefault());
        }
        ApplicationDescriptor applicationDescriptor = this.selectedProfile.getApplicationDescriptor();
        if (!StringUtils.substringAfterLast(applicationDescriptor.getGamePath(), "\\").equals("")) {
            applicationDescriptor.setGamePath(applicationDescriptor.getGamePath() + File.separatorChar);
        }
    }

    @Override
    public ApplicationDescriptor get() {
        return this.selectedProfile.getApplicationDescriptor();
    }

    @Override
    public void set(ApplicationDescriptor descriptor) {
        this.selectedProfile.setApplicationDescriptor(descriptor);
    }

    @Override
    public ApplicationDescriptor getDefault() {
        ApplicationDescriptor descriptor = new ApplicationDescriptor();
        descriptor.setNotifierStatus(WhisperNotifierStatus.ALWAYS);
        descriptor.setMinOpacity(100);
        descriptor.setMaxOpacity(100);
        descriptor.setGamePath("");
        descriptor.setShowOnStartUp(true);
        descriptor.setItemsGridEnable(true);
        descriptor.setCheckOutUpdate(true);
        return descriptor;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setApplicationDescriptor(this.getDefault());
    }
}
