package com.mercury.platform.shared.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class DataSource {
    private String configurationFilePath;
}
