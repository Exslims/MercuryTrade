package com.mercury.platform.shared.config.descriptor;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProfileDescriptor {
    private String profileName;
    private boolean selected;
    private Map<String,FrameDescriptor> frameDescriptorMap;
    private Map<String,SoundDescriptor> soundDescriptorMap;
    private ApplicationDescriptor applicationDescriptor;
    private NotificationDescriptor notificationDescriptor;
    private ScannerDescriptor scannerDescriptor;
    private Map<String,Float> scaleDataMap;
    private List<StashTabDescriptor> stashTabDescriptors;
}
