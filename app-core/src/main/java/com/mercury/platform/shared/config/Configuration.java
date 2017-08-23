package com.mercury.platform.shared.config;

public class Configuration {
    private static ConfigManager configManager;

    public static ConfigManager get() {
        return configManager;
    }

    public static void set(ConfigManager configManager) {
        Configuration.configManager = configManager;
    }
}
