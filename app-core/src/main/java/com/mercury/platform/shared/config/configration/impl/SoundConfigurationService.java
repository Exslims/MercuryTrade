package com.mercury.platform.shared.config.configration.impl;

import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.SoundDescriptor;

import java.util.*;


public class SoundConfigurationService extends BaseConfigurationService<Map<String,SoundDescriptor>> implements KeyValueConfigurationService<SoundDescriptor,String> {
    private static final String OBJECT_KEY = "soundConfiguration";
    public SoundConfigurationService(ConfigurationSource dataSource) {
        super(dataSource);
    }

    @Override
    public void load(){
        this.data = jsonHelper.readMapData(OBJECT_KEY, new TypeToken<Map<String, SoundDescriptor>>() {});
        if(data == null) {
            this.toDefault();
        }
    }

    @Override
    public SoundDescriptor get(String key) {
        return this.data.computeIfAbsent(key, k -> this.getDefault().get(key));
    }

    @Override
    public Map<String, SoundDescriptor> getMap() {
        return this.data;
    }

    @Override
    public void save() {
        jsonHelper.writeMapObject(OBJECT_KEY,this.data);
    }


    @Override
    public Map<String, SoundDescriptor> getDefault() {
        Map<String,SoundDescriptor> defaultSettings = new HashMap<>();
        defaultSettings.put("notification",new SoundDescriptor("app/notification.wav",0f));
        defaultSettings.put("chat_scanner",new SoundDescriptor("app/chat-filter.wav",0f));
        defaultSettings.put("clicks",new SoundDescriptor("default",0f));
        defaultSettings.put("update",new SoundDescriptor("default",0f));
        return defaultSettings;
    }

    @Override
    public void toDefault() {
        Map<String, SoundDescriptor> defaultSt = this.getDefault();
        this.data = defaultSt;
        jsonHelper.writeMapObject(OBJECT_KEY,defaultSt);
    }
}
