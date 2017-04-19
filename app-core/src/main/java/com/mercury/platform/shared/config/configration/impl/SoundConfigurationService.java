package com.mercury.platform.shared.config.configration.impl;

import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.config.configration.BaseKeyValueConfigurationService;
import com.mercury.platform.shared.entity.SoundDescriptor;

import java.util.*;


public class SoundConfigurationService extends BaseKeyValueConfigurationService<SoundDescriptor,String> {
    private static final String OBJECT_KEY = "sound";
    private Map<String,SoundDescriptor> data;
    public SoundConfigurationService(DataSource dataSource) {
        super(dataSource);
        this.data = new HashMap<>();
    }

    @Override
    public void load(){
        this.data = jsonHelper.readMapData(OBJECT_KEY, new TypeToken<Map<String, SoundDescriptor>>() {});
        if(data == null) {
            toDefault();
        }
    }

    @Override
    public SoundDescriptor get(String key) {
        return this.data.computeIfAbsent(key, k -> getDefaultMap().get(key));
    }

    @Override
    public Map<String, SoundDescriptor> getMap() {
        return this.data;
    }

    @Override
    public void save() {
        jsonHelper.writeMapObject(OBJECT_KEY,this.data);
    }

    private void toDefault(){
        Map<String, SoundDescriptor> defaultSt = getDefaultMap();
        this.data = defaultSt;
        jsonHelper.writeMapObject(OBJECT_KEY,defaultSt);
    }
    private Map<String,SoundDescriptor> getDefaultMap(){
        Map<String,SoundDescriptor> defaultSettings = new HashMap<>();
        defaultSettings.put("notification",new SoundDescriptor("app/notification.wav",0f));
        defaultSettings.put("chat_scanner",new SoundDescriptor("app/chat-filter.wav",0f));
        defaultSettings.put("clicks",new SoundDescriptor("default",0f));
        defaultSettings.put("update",new SoundDescriptor("default",0f));
        return defaultSettings;

    }

    @Override
    public SoundDescriptor getDefault() {
        return new SoundDescriptor("app/notification.wav",0f);
    }
}
