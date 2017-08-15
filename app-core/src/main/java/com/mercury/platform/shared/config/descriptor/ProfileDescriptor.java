package com.mercury.platform.shared.config.descriptor;

import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProfileDescriptor {
    private String name;
    private boolean selected;
    private Map<String,FrameDescriptor> frameDescriptorMap;
    private Map<String,SoundDescriptor> soundDescriptorMap;
    private ApplicationDescriptor applicationDescriptor;
    private NotificationSettingsDescriptor notificationSettingsDescriptor;
    private ScannerDescriptor scannerDescriptor;
    private Map<String,Float> scaleDataMap;
    private Map<String,HotKeyDescriptor> hotKeysDataMap;
    private List<StashTabDescriptor> stashTabDescriptors;
    private List<AdrProfileDescriptor> adrProfileDescriptorList;
    private List<String> iconBundleList;
}
