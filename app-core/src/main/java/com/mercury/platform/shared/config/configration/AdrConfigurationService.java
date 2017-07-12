package com.mercury.platform.shared.config.configration;


import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;

public interface AdrConfigurationService extends ListConfigurationService<AdrProfileDescriptor>{
    AdrIconDescriptor getDefaultIcon();
    AdrProgressBarDescriptor getDefaultProgressBar();
    AdrGroupDescriptor getDefaultGroup();
}
