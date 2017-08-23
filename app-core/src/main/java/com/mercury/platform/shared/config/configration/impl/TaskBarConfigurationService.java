package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.TaskBarDescriptor;

public class TaskBarConfigurationService extends BaseConfigurationService<TaskBarDescriptor> implements PlainConfigurationService<TaskBarDescriptor> {
    public TaskBarConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public TaskBarDescriptor getDefault() {
        return new TaskBarDescriptor();
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setTaskBarDescriptor(this.getDefault());
    }

    @Override
    public TaskBarDescriptor get() {
        return this.selectedProfile.getTaskBarDescriptor();
    }

    @Override
    public void set(TaskBarDescriptor descriptor) {
        this.selectedProfile.setTaskBarDescriptor(descriptor);
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getTaskBarDescriptor() == null) {
            this.selectedProfile.setTaskBarDescriptor(this.getDefault());
        }
    }
}
