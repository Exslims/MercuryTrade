package com.mercury.platform.shared.config;


public class MercuryConfigurationSource extends ConfigurationSource {
    public MercuryConfigurationSource() {
        super(System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade",
                System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\configuration.json");
    }
}
