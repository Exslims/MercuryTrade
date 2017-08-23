package com.mercury.platform.shared.config.configration;


import com.mercury.platform.shared.config.descriptor.adr.*;

public interface AdrConfigurationService extends ListConfigurationService<AdrProfileDescriptor> {
    AdrIconDescriptor getDefaultIcon();

    AdrProgressBarDescriptor getDefaultProgressBar();

    AdrTrackerGroupDescriptor getDefaultIconGroup();

    AdrTrackerGroupDescriptor getDefaultPBGroup();

    AdrCaptureDescriptor getDefaultCapture();
}
