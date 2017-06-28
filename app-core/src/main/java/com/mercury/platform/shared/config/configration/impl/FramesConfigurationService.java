package com.mercury.platform.shared.config.configration.impl;

import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;

import java.util.Map;


public class FramesConfigurationService extends BaseConfigurationService<Map<String,FrameDescriptor>> implements KeyValueConfigurationService<FrameDescriptor,String> {
    private static final String OBJECT_KEY = "framesConfiguration";
    public FramesConfigurationService(ConfigurationSource dataSource) {
        super(dataSource);
    }

    @Override
    public void load() {
        this.data = jsonHelper.readMapData(OBJECT_KEY, new TypeToken<Map<String, FrameDescriptor>>() {});
        if(data == null) {
            this.toDefault();
        }
    }

    @Override
    public FrameDescriptor get(String key) {
        return null;
    }

    @Override
    public Map<String, FrameDescriptor> getMap() {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public Map<String, FrameDescriptor> getDefault() {
        return null;
    }

    @Override
    public void toDefault() {

    }
}
