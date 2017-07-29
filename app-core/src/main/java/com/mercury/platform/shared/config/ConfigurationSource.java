package com.mercury.platform.shared.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class ConfigurationSource {
    private String configurationPath;
    private String configurationFilePath;
}
