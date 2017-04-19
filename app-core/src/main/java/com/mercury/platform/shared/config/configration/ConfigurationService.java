package com.mercury.platform.shared.config.configration;

import java.io.IOException;

public interface ConfigurationService {
    void load() throws IOException;
    void save();
}
