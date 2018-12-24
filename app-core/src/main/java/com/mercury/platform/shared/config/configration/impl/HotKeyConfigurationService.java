package com.mercury.platform.shared.config.configration.impl;


import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        outNDataList.add(new HotKeyPair(HotKeyType.N_WHO_IS, new HotKeyDescriptor()));
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
        this.selectedProfile.setHotkeysSettingsDescriptor(this.getDefault());
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getHotkeysSettingsDescriptor() == null) {
            this.selectedProfile.setHotkeysSettingsDescriptor(this.getDefault());
        }

        this.selectedProfile.getHotkeysSettingsDescriptor()
                .setOutNHotKeysList(this.removeNullTypesFrom(
                        this.selectedProfile.getHotkeysSettingsDescriptor().getOutNHotKeysList()));

        this.selectedProfile.getHotkeysSettingsDescriptor()
                .setIncNHotKeysList(this.removeNullTypesFrom(
                        this.selectedProfile.getHotkeysSettingsDescriptor().getIncNHotKeysList()));

        this.selectedProfile.getHotkeysSettingsDescriptor()
                .setScannerNHotKeysList(this.removeNullTypesFrom(
                        this.selectedProfile.getHotkeysSettingsDescriptor().getScannerNHotKeysList()));

        if (this.selectedProfile.getHotkeysSettingsDescriptor()
                .getOutNHotKeysList().stream()
                .filter(it -> HotKeyType.N_WHO_IS.equals(it.getType()))
                .findAny().orElse(null) == null) {
            this.selectedProfile.getHotkeysSettingsDescriptor()
                    .getOutNHotKeysList().add(new HotKeyPair(HotKeyType.N_WHO_IS, new HotKeyDescriptor()));
        }

        if (this.selectedProfile.getHotkeysSettingsDescriptor()
                .getScannerNHotKeysList().stream()
                .filter(it -> HotKeyType.N_WHO_IS.equals(it.getType()))
                .findAny().orElse(null) == null) {
            this.selectedProfile.getHotkeysSettingsDescriptor()
                    .getScannerNHotKeysList().add(new HotKeyPair(HotKeyType.N_WHO_IS, new HotKeyDescriptor()));
        }
    }

    @Override
    public HotKeysSettingsDescriptor get() {
        return this.selectedProfile.getHotkeysSettingsDescriptor();
    }

    @Override
    public void set(HotKeysSettingsDescriptor descriptor) {
        this.selectedProfile.setHotkeysSettingsDescriptor(descriptor);
    }

    private List<HotKeyPair> removeNullTypesFrom(List<HotKeyPair> list) {
        return list.stream().filter(it -> it.getType() != null).collect(Collectors.toList());
    }
}
