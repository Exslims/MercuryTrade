package com.mercury.platform.shared.config.configration.impl.adr;


import com.mercury.platform.shared.config.configration.AdrConfigurationService;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.shared.config.json.JSONHelper;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdrConfigurationServiceMock extends BaseConfigurationService<List<AdrProfileDescriptor>> implements AdrConfigurationService {
    private List<AdrProfileDescriptor> currentProfiles;

    public AdrConfigurationServiceMock(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public List<AdrProfileDescriptor> getEntities() {
        return this.selectedProfile.getAdrProfileDescriptorList();
    }


    @Override
    public List<AdrProfileDescriptor> getDefault() {
        return this.currentProfiles;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setAdrProfileDescriptorList(this.getDefault());
    }

    @Override
    public void validate() {
        this.currentProfiles = Arrays.stream(new AdrProfileDescriptor[]{this.getShowCaseProfile(), this.getDefaultProfile("Default profile")}).collect(Collectors.toList());
        if (this.selectedProfile.getAdrProfileDescriptorList() == null) {
            this.selectedProfile.setAdrProfileDescriptorList(this.getDefault());
        }
    }

    private AdrProfileDescriptor getDefaultProfile(String profileName) {
        AdrProfileDescriptor profileDescriptor = new AdrProfileDescriptor();
        profileDescriptor.setProfileName(profileName);
        profileDescriptor.setSelected(true);
        return profileDescriptor;
    }

    @Override
    public AdrIconDescriptor getDefaultIcon() {
        AdrIconDescriptor icon = new AdrIconDescriptor();
        icon.setType(AdrComponentType.ICON);
        return icon;
    }

    @Override
    public AdrProgressBarDescriptor getDefaultProgressBar() {
        AdrProgressBarDescriptor progressBar = new AdrProgressBarDescriptor();
        progressBar.setTitle("progress bar");
        progressBar.setSize(new Dimension(240, 30));
        progressBar.setType(AdrComponentType.PROGRESS_BAR);
        progressBar.setForegroundColor(new Color(22, 126, 138));
        return progressBar;
    }

    @Override
    public AdrTrackerGroupDescriptor getDefaultIconGroup() {
        AdrTrackerGroupDescriptor groupDescriptor = this.getDefaultGroup();
        groupDescriptor.setTitle("icon group");
        groupDescriptor.setContentType(AdrTrackerGroupContentType.ICONS);
        return groupDescriptor;
    }

    @Override
    public AdrTrackerGroupDescriptor getDefaultPBGroup() {
        AdrTrackerGroupDescriptor groupDescriptor = this.getDefaultGroup();
        groupDescriptor.setTitle("progress bar group");
        groupDescriptor.setSize(new Dimension(240, 30));
        groupDescriptor.setContentType(AdrTrackerGroupContentType.PROGRESS_BARS);
        return groupDescriptor;
    }

    private AdrTrackerGroupDescriptor getDefaultGroup() {
        AdrTrackerGroupDescriptor groupDescriptor = new AdrTrackerGroupDescriptor();
        groupDescriptor.setTitle("group");
        groupDescriptor.setType(AdrComponentType.TRACKER_GROUP);
        groupDescriptor.setOrientation(AdrComponentOrientation.VERTICAL);
        return groupDescriptor;
    }

    private AdrProfileDescriptor getShowCaseProfile() {
        AdrProfileDescriptor profileDescriptor = new AdrProfileDescriptor();
        profileDescriptor.setProfileName("Showcase");
        profileDescriptor.setSelected(false);
        JSONHelper jsonHelper = new JSONHelper();
        profileDescriptor.setContents(jsonHelper.getJsonAsObjectFromFile("notes/showcase-profile.json"));
        return profileDescriptor;
    }

    @Override
    public AdrCaptureDescriptor getDefaultCapture() {
        AdrCaptureDescriptor descriptor = new AdrCaptureDescriptor();
        descriptor.setTitle("Capture");
        descriptor.setType(AdrComponentType.CAPTURE);
        descriptor.setCaptureLocation(new Point(descriptor.getLocation().x + 80, descriptor.getLocation().y));
        return descriptor;
    }
}
