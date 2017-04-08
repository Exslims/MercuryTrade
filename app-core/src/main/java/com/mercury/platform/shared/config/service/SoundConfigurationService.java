package com.mercury.platform.shared.config.service;

import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.config.DataSource;
import com.mercury.platform.shared.entity.KeyData;
import com.mercury.platform.shared.entity.SoundDescriptor;

import java.util.*;


public class SoundConfigurationService extends BaseConfigurationService<SoundDescriptor,String> {
    private List<KeyData<SoundDescriptor>> data;
    public SoundConfigurationService(DataSource dataSource) {
        super(dataSource);
        this.data = new ArrayList<>();
    }

    @Override
    public void load(){
        jsonHelper.readArrayKeyData(new KeyData<>("sound",new ArrayList<>()),new TypeToken<KeyData<List<SoundDescriptor>>>(){})
                .subscribe(data -> {
                    if(data == null){
                        toDefault();
                    }else {
                        System.out.println(data);
                    }
                });
    }

    @Override
    public SoundDescriptor get(String key) {
        Optional<KeyData<SoundDescriptor>> first = this.data.stream()
                .filter(item -> item.getKey().equals(key))
                .findFirst();

        if(!first.isPresent()){
            SoundDescriptor data = getDefault()
                    .getData()
                    .stream()
                    .filter(item -> item.getKey().equals(key))
                    .findFirst()
                    .get()
                    .getData();
            this.data.add(new KeyData<>(key,data));
            save();
            return data;
        }
        return first.get().getData();
    }

    @Override
    public void save() {
//        jsonHelper.writeArrayObject(new KeyData<>("sound",this.data));
    }

    private void toDefault(){
        KeyData<List<KeyData<SoundDescriptor>>> defaultSt = getDefault();
        this.data = defaultSt.getData();
        jsonHelper.writeArrayObject("sound",defaultSt.getData(),new TypeToken<KeyData<List<KeyData<SoundDescriptor>>>>(){});
    }
    private KeyData<List<KeyData<SoundDescriptor>>> getDefault(){
        List<KeyData<SoundDescriptor>> defaultSettings = new ArrayList<>();
        defaultSettings.add(new KeyData<>("notification",new SoundDescriptor("app/notification.wav",0f)));
        defaultSettings.add(new KeyData<>("chat_scanner",new SoundDescriptor("app/chat-filter.wav",0f)));
        defaultSettings.add(new KeyData<>("clicks",new SoundDescriptor("default",0f)));
        defaultSettings.add(new KeyData<>("update",new SoundDescriptor("default",0f)));
        return new KeyData<>("sound",defaultSettings);

    }
}
