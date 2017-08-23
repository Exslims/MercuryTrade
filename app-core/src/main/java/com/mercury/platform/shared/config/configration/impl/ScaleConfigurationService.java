package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;

import java.util.HashMap;
import java.util.Map;


public class ScaleConfigurationService extends BaseConfigurationService<Map<String, Float>> implements KeyValueConfigurationService<String, Float> {
    public ScaleConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public void validate() {
        if (this.selectedProfile.getScaleDataMap() == null) {
            this.selectedProfile.setScaleDataMap(this.getDefault());
        }
    }

    @Override
    public Float get(String key) {
        return this.selectedProfile.getScaleDataMap().computeIfAbsent(key, k -> {
            this.selectedProfile.getScaleDataMap().put(key, this.getDefault().get(key));
            MercuryStoreCore.saveConfigSubject.onNext(true);
            return this.getDefault().get(key);
        });
    }

    @Override
    public Map<String, Float> getDefault() {
        Map<String, Float> scaleData = new HashMap<>();
        scaleData.put("notification", 1f);
        scaleData.put("taskbar", 1f);
        scaleData.put("itemcell", 1f);
        scaleData.put("other", 1f);
        return scaleData;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setScaleDataMap(this.getDefault());
    }


    @Override
    public Map<String, Float> getMap() {
        return this.selectedProfile.getScaleDataMap();
    }

    @Override
    public void set(Map<String, Float> map) {
        this.selectedProfile.setScaleDataMap(map);
    }
}
