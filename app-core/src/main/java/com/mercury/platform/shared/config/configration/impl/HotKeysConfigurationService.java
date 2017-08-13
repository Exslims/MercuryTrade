package com.mercury.platform.shared.config.configration.impl;


import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.util.HashMap;
import java.util.Map;

public class HotKeysConfigurationService extends BaseConfigurationService<Map<String,HotKeyDescriptor>> implements KeyValueConfigurationService<String,HotKeyDescriptor> {
    public HotKeysConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public Map<String, HotKeyDescriptor> getDefault() {
        Map<String, HotKeyDescriptor> keyMap = new HashMap<>();
        keyMap.put(HotKeyType.INVITE_PLAYER.name(),new HotKeyDescriptor("Alt + 1", NativeKeyEvent.VC_1,'1',true,false,false));
        keyMap.put(HotKeyType.TRADE_PLAYER.name(),new HotKeyDescriptor("Alt + 2",NativeKeyEvent.VC_2,'2',true,false,false));
        keyMap.put(HotKeyType.KICK_PLAYER.name(),new HotKeyDescriptor("Alt + 3",NativeKeyEvent.VC_3,'3',true,false,false));
        keyMap.put(HotKeyType.STILL_INTERESTING.name(),new HotKeyDescriptor("Alt + 4",NativeKeyEvent.VC_4,'4',true,false,false));
        keyMap.put(HotKeyType.CLOSE_NOTIFICATION.name(),new HotKeyDescriptor("Alt + 5",NativeKeyEvent.VC_5,'5',true,false,false));
        return keyMap;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setHotKeysDataMap(this.getDefault());
    }

    @Override
    public HotKeyDescriptor get(String key) {
        return this.selectedProfile.getHotKeysDataMap().computeIfAbsent(key, k -> {
            this.selectedProfile.getHotKeysDataMap().put(key,this.getDefault().get(key));
            MercuryStoreCore.saveConfigSubject.onNext(true);
            return this.getDefault().get(key);
        });
    }

    @Override
    public Map<String, HotKeyDescriptor> getMap() {
        return this.selectedProfile.getHotKeysDataMap();
    }

    @Override
    public void set(Map<String, HotKeyDescriptor> map) {
        this.selectedProfile.setHotKeysDataMap(map);
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getHotKeysDataMap() == null){
            this.selectedProfile.setHotKeysDataMap(this.getDefault());
        }
        this.getMap().values().forEach(it -> {
            if(it.getTitle().equals("")){
                it.setTitle("...");
            }
        });
    }
}
