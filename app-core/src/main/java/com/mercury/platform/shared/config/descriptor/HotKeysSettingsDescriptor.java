package com.mercury.platform.shared.config.descriptor;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class HotKeysSettingsDescriptor implements Serializable {
    private List<HotKeyPair> incNHotKeysList = new ArrayList<>();
    private List<HotKeyPair> outNHotKeysList = new ArrayList<>();
    private List<HotKeyPair> scannerNHotKeysList = new ArrayList<>();
}
