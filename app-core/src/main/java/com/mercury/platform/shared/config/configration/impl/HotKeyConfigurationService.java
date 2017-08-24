package com.mercury.platform.shared.config.configration.impl;


import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;

import java.util.ArrayList;
import java.util.List;

public class HotKeyConfigurationService extends BaseConfigurationService<HotKeysSettingsDescriptor> implements PlainConfigurationService<HotKeysSettingsDescriptor> {
    public HotKeyConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public HotKeysSettingsDescriptor getDefault() {
        HotKeysSettingsDescriptor hotKeysSettingsDescriptor = new HotKeysSettingsDescriptor();
        List<HotKeyPair> incNDataList = new ArrayList<>();
        incNDataList.add(new HotKeyPair(HotKeyType.N_INVITE_PLAYER, new HotKeyDescriptor()));
        incNDataList.add(new HotKeyPair(HotKeyType.N_TRADE_PLAYER, new HotKeyDescriptor()));
        incNDataList.add(new HotKeyPair(HotKeyType.N_KICK_PLAYER, new HotKeyDescriptor()));
        incNDataList.add(new HotKeyPair(HotKeyType.N_STILL_INTERESTING, new HotKeyDescriptor()));
        incNDataList.add(new HotKeyPair(HotKeyType.N_OPEN_CHAT, new HotKeyDescriptor()));
        incNDataList.add(new HotKeyPair(HotKeyType.N_CLOSE_NOTIFICATION, new HotKeyDescriptor()));

        List<HotKeyPair> outNDataList = new ArrayList<>();
        outNDataList.add(new HotKeyPair(HotKeyType.N_VISITE_HIDEOUT, new HotKeyDescriptor()));
        outNDataList.add(new HotKeyPair(HotKeyType.N_TRADE_PLAYER, new HotKeyDescriptor()));
        outNDataList.add(new HotKeyPair(HotKeyType.N_LEAVE, new HotKeyDescriptor()));
        outNDataList.add(new HotKeyPair(HotKeyType.N_OPEN_CHAT, new HotKeyDescriptor()));
        outNDataList.add(new HotKeyPair(HotKeyType.N_REPEAT_MESSAGE, new HotKeyDescriptor()));
        outNDataList.add(new HotKeyPair(HotKeyType.N_CLOSE_NOTIFICATION, new HotKeyDescriptor()));

        List<HotKeyPair> scannerNDataList = new ArrayList<>();
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_QUICK_RESPONSE, new HotKeyDescriptor()));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_VISITE_HIDEOUT, new HotKeyDescriptor()));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_TRADE_PLAYER, new HotKeyDescriptor()));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_LEAVE, new HotKeyDescriptor()));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_OPEN_CHAT, new HotKeyDescriptor()));
        scannerNDataList.add(new HotKeyPair(HotKeyType.N_CLOSE_NOTIFICATION, new HotKeyDescriptor()));

        hotKeysSettingsDescriptor.setIncNHotKeysList(incNDataList);
        hotKeysSettingsDescriptor.setOutNHotKeysList(outNDataList);
        hotKeysSettingsDescriptor.setScannerNHotKeysList(scannerNDataList);
        return hotKeysSettingsDescriptor;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setHotKeysSettingsDescriptor(this.getDefault());
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getHotKeysSettingsDescriptor() == null) {
            this.selectedProfile.setHotKeysSettingsDescriptor(this.getDefault());
        }
        List<HotKeyPair> stubNList = new ArrayList<>(this.selectedProfile.getHotKeysSettingsDescriptor().getIncNHotKeysList());
        stubNList.forEach(it -> {
            if (it.getType().equals(HotKeyType.N_SWITCH_CHAT)) {
                this.selectedProfile.getHotKeysSettingsDescriptor().getIncNHotKeysList().remove(it);
            }
        });
        List<HotKeyPair> stubOList = new ArrayList<>(this.selectedProfile.getHotKeysSettingsDescriptor().getOutNHotKeysList());
        HotKeyPair hotKeyPair = stubOList.stream().filter(it -> it.getType().equals(HotKeyType.N_REPEAT_MESSAGE)).findAny().orElse(null);
        if (hotKeyPair == null) {
            this.selectedProfile.getHotKeysSettingsDescriptor().getOutNHotKeysList().add(new HotKeyPair(HotKeyType.N_REPEAT_MESSAGE, new HotKeyDescriptor()));
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
