package com.mercury.platform.shared.config.descriptor;

import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProfileDescriptor {
    private String name;
    private boolean selected;
    private Map<String, FrameDescriptor> frameDescriptorMap;
    private Map<String, SoundDescriptor> soundDescriptorMap;
    private ApplicationDescriptor applicationDescriptor;
    private NotificationSettingsDescriptor notificationDescriptor;
    private TaskBarDescriptor taskBarDescriptor;
    private ScannerDescriptor scannerDescriptor;
    private Map<String, Float> scaleDataMap;
    private HotKeysSettingsDescriptor hotkeysSettingsDescriptor;
    private List<StashTabDescriptor> stashTabDescriptors;
    private List<AdrProfileDescriptor> adrProfileDescriptorList;
    private List<String> iconBundleList;
}
