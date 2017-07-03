package com.mercury.platform.shared.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
abstract class ConfigurationSource {
    private String configurationFilePath;
}
