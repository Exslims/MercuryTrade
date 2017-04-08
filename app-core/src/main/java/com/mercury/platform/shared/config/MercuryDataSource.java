package com.mercury.platform.shared.config;

/**
 * Created by Константин on 08.04.2017.
 */
public class MercuryDataSource extends DataSource {
    protected MercuryDataSource() {
        super(System.getenv("USERPROFILE") + "\\AppData\\Local\\MercuryTrade\\app-config.json");
    }
}
