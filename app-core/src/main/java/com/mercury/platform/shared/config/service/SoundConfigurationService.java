package com.mercury.platform.shared.config.service;

import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.entity.SoundDescriptor;

import java.util.*;


public class SoundConfigurationService extends BaseConfigurationService<SoundDescriptor,String> {
    private Map<String,SoundDescriptor> data;
    public SoundConfigurationService(DataSource dataSource) {
        super(dataSource);
        this.data = new HashMap<>();
    }

    @Override
    public void load(){
        this.data = jsonHelper.readMapData("sound", new TypeToken<Map<String, SoundDescriptor>>() {});
        if(data == null) {
            toDefault();
        }
        System.out.println(this.data);
    }

    @Override
    public SoundDescriptor get(String key) {
        return this.data.computeIfAbsent(key, k -> getDefault().get(key));
    }

    @Override
    public void save() {
//        jsonHelper.writeMapObject(new KeyData<>("sound",this.data));
    }

    private void toDefault(){
        Map<String, SoundDescriptor> defaultSt = getDefault();
        jsonHelper.writeMapObject("sound",defaultSt);
    }
    private Map<String,SoundDescriptor> getDefault(){
        Map<String,SoundDescriptor> defaultSettings = new HashMap<>();
        defaultSettings.put("notification",new SoundDescriptor("app/notification.wav",0f));
        defaultSettings.put("chat_scanner",new SoundDescriptor("app/chat-filter.wav",0f));
        defaultSettings.put("clicks",new SoundDescriptor("default",0f));
        defaultSettings.put("update",new SoundDescriptor("default",0f));
        return defaultSettings;

    }
}
