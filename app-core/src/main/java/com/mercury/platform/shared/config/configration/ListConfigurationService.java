package com.mercury.platform.shared.config.configration;


import java.util.List;

public interface ListConfigurationService<T> extends ConfigurationService, HasDefault<T> {
    List<T> getEntity();
}
