package com.mercury.platform.shared.config.configration.impl;


import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.util.*;

public class HotKeyConfigurationService extends BaseConfigurationService<HotKeysSettingsDescriptor> implements PlainConfigurationService<HotKeysSettingsDescriptor> {
    public HotKeyConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public HotKeysSettingsDescriptor getDefault() {
        HotKeysSettingsDescriptor hotKeysSettingsDescriptor = new HotKeysSettingsDescriptor();
        List<HotKeyPair> incNDataList = new ArrayList<>();
        incNDataList.add(new HotKeyPair(HotKeyType.N_INVITE_PLAYER,new HotKeyDescriptor("F1", NativeKeyEvent.VC_F1,false,false,false)));
        incNDataList.add(new HotKeyPair(HotKeyType.N_TRADE_PLAYER,new HotKeyDescriptor("F2",NativeKeyEvent.VC_F2,false,false,false)));
        incNDataList.add(new HotKeyPair(HotKeyType.N_KICK_PLAYER,new HotKeyDescriptor("F3",NativeKeyEvent.VC_F3,false,false,false)));
        incNDataList.add(new HotKeyPair(HotKeyType.N_STILL_INTERESTING,new HotKeyDescriptor("F4",NativeKeyEvent.VC_F4,false,false,false)));
        incNDataList.add(new HotKeyPair(HotKeyType.N_OPEN_CHAT,new HotKeyDescriptor("F5",NativeKeyEvent.VC_F5,false,false,false)));
        incNDataList.add(new HotKeyPair(HotKeyType.N_SWITCH_CHAT,new HotKeyDescriptor("F6",NativeKeyEvent.VC_F6,false,false,false)));
        incNDataList.add(new HotKeyPair(HotKeyType.N_CLOSE_NOTIFICATION,new HotKeyDescriptor("F7",NativeKeyEvent.VC_F7,false,false,false)));

        List<HotKeyPair> outNDataList = new ArrayList<>();
        outNDataList.add(new HotKeyPair(HotKeyType.N_VISITE_HIDEOUT,new HotKeyDescriptor("F1", NativeKeyEvent.VC_F1,false,false,false)));
        outNDataList.add(new HotKeyPair(HotKeyType.N_TRADE_PLAYER,new HotKeyDescriptor("F2",NativeKeyEvent.VC_F2,false,false,false)));
        outNDataList.add(new HotKeyPair(HotKeyType.N_LEAVE,new HotKeyDescriptor("F3",NativeKeyEvent.VC_F3,false,false,false)));
        outNDataList.add(new HotKeyPair(HotKeyType.N_OPEN_CHAT,new HotKeyDescriptor("F4",NativeKeyEvent.VC_F4,false,false,false)));
        outNDataList.add(new HotKeyPair(HotKeyType.N_CLOSE_NOTIFICATION,new HotKeyDescriptor("F5",NativeKeyEvent.VC_F5,false,false,false)));

        List<HotKeyPair> scannerNDataList = new ArrayList<>();
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_QUICK_RESPONSE,new HotKeyDescriptor("F1", NativeKeyEvent.VC_F1,false,false,false)));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_VISITE_HIDEOUT,new HotKeyDescriptor("F2", NativeKeyEvent.VC_F2,false,false,false)));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_TRADE_PLAYER,new HotKeyDescriptor("F3",NativeKeyEvent.VC_F3,false,false,false)));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_LEAVE,new HotKeyDescriptor("F4",NativeKeyEvent.VC_F4,false,false,false)));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_OPEN_CHAT,new HotKeyDescriptor("F5",NativeKeyEvent.VC_F5,false,false,false)));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_CLOSE_NOTIFICATION,new HotKeyDescriptor("F6",NativeKeyEvent.VC_F6,false,false,false)));

        List<HotKeyPair> taskBarNDataList = new ArrayList<>();
        taskBarNDataList.add(new HotKeyPair(HotKeyType.T_TO_HIDEOUT,new HotKeyDescriptor("F10",NativeKeyEvent.VC_F10,false,false,false)));
//        taskBarNDataList.add(new HotKeyPair(HotKeyType.T_DND,new HotKeyDescriptor("F2",NativeKeyEvent.VC_F2,false,false,false)));

        hotKeysSettingsDescriptor.setIncNHotKeysList(incNDataList);
        hotKeysSettingsDescriptor.setOutNHotKeysList(outNDataList);
        hotKeysSettingsDescriptor.setScannerNHotKeysList(scannerNDataList);
        hotKeysSettingsDescriptor.setTaskBarNHotKeysList(taskBarNDataList);
        return hotKeysSettingsDescriptor;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setHotKeysSettingsDescriptor(this.getDefault());
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getHotKeysSettingsDescriptor() == null){
            this.selectedProfile.setHotKeysSettingsDescriptor(this.getDefault());
        }
    }

    @Override
    public HotKeysSettingsDescriptor get() {
        return this.selectedProfile.getHotKeysSettingsDescriptor();
    }

    @Override
    public void set(HotKeysSettingsDescriptor descriptor) {
        this.selectedProfile.setHotKeysSettingsDescriptor(descriptor);
    }
}
