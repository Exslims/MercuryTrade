package com.mercury.platform.shared.config.configration.impl;


import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

import java.util.HashMap;
import java.util.Map;

public class HotKeysConfigurationService extends BaseConfigurationService<Map<String,HotKeyDescriptor>> implements KeyValueConfigurationService<String,HotKeyDescriptor> {
    public HotKeysConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public Map<String, HotKeyDescriptor> getDefault() {
        Map<String, HotKeyDescriptor> keyMap = new HashMap<>();
        keyMap.put(HotKeyType.INVITE_PLAYER.name(),new HotKeyDescriptor("",GlobalKeyEvent.VK_1,'1',true,false,false,false));
        keyMap.put(HotKeyType.TRADE_PLAYER.name(),new HotKeyDescriptor("",GlobalKeyEvent.VK_2,'2',true,false,false,false));
        keyMap.put(HotKeyType.KICK_PLAYER.name(),new HotKeyDescriptor("",GlobalKeyEvent.VK_3,'3',true,false,false,false));
        keyMap.put(HotKeyType.STILL_INTERESTING.name(),new HotKeyDescriptor("",GlobalKeyEvent.VK_4,'4',true,false,false,false));
        keyMap.put(HotKeyType.CLOSE_NOTIFICATION.name(),new HotKeyDescriptor("",GlobalKeyEvent.VK_5,'5',true,false,false,false));
        keyMap.put(HotKeyType.EXPAND_ALL.name(),new HotKeyDescriptor("",GlobalKeyEvent.VK_1,'1',false,false,true,false));
        keyMap.put("button_1",new HotKeyDescriptor("",GlobalKeyEvent.VK_1,'1',false,true,false,false));
        keyMap.put("button_2",new HotKeyDescriptor("",GlobalKeyEvent.VK_1,'2',false,true,false,false));
        keyMap.put("button_3",new HotKeyDescriptor("",GlobalKeyEvent.VK_1,'3',false,true,false,false));
        keyMap.put("button_4",new HotKeyDescriptor("",GlobalKeyEvent.VK_1,'4',false,true,false,false));
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
    public void validate() {
        if (this.selectedProfile.getHotKeysDataMap() == null){
            this.selectedProfile.setHotKeysDataMap(this.getDefault());
        }
    }
}
