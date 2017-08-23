package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.ScannerDescriptor;

public class ScannerConfigurationService extends BaseConfigurationService<ScannerDescriptor> implements PlainConfigurationService<ScannerDescriptor> {
    public ScannerConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public ScannerDescriptor getDefault() {
        ScannerDescriptor scannerDescriptor = new ScannerDescriptor();
        scannerDescriptor.setWords("!wtb, uber, boss, perandus");
        scannerDescriptor.setResponseMessage("invite me pls");
        return scannerDescriptor;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setScannerDescriptor(this.getDefault());
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getScannerDescriptor() == null) {
            this.selectedProfile.setScannerDescriptor(this.getDefault());
        }
    }

    @Override
    public ScannerDescriptor get() {
        return this.selectedProfile.getScannerDescriptor();
    }

    @Override
    public void set(ScannerDescriptor descriptor) {
        this.selectedProfile.setScannerDescriptor(descriptor);
    }
}
