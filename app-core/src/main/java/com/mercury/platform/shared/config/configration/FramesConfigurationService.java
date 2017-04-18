package com.mercury.platform.shared.config.configration;

import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.entity.FrameSettings;

import java.util.Map;


public class FramesConfigurationService extends BaseConfigurationService<FrameSettings,String> {
    public FramesConfigurationService(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void load() {

    }

    @Override
    public FrameSettings get(String key) {
        return null;
    }

    @Override
    public Map<String, FrameSettings> getMap() {
        return null;
    }

    @Override
    public void save() {

    }
}
