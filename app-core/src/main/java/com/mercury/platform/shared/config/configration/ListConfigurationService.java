package com.mercury.platform.shared.config.configration;


import java.util.List;

public interface ListConfigurationService<T> {
    List<T> getEntities();
}
