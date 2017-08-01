package com.mercury.platform.shared.config.configration.impl.adr;


import com.mercury.platform.shared.config.configration.AdrConfigurationService;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AdrConfigurationServiceMock extends BaseConfigurationService<List<AdrProfileDescriptor>> implements AdrConfigurationService {
    private List<AdrProfileDescriptor> currentProfiles;
    public AdrConfigurationServiceMock(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public List<AdrProfileDescriptor> getEntities() {
        return this.selectedProfile.getAdrProfileDescriptorList();
//        return this.getDefault();
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
        this.currentProfiles = Arrays.stream(new AdrProfileDescriptor[]{this.getShowcaseProfile(),this.getDefaultProfile("Default profile")}).collect(Collectors.toList());
        if(this.selectedProfile.getAdrProfileDescriptorList() == null){
            this.selectedProfile.setAdrProfileDescriptorList(this.getDefault());
        }
    }
    private AdrProfileDescriptor getDefaultProfile(String profileName){
        AdrProfileDescriptor profileDescriptor = new AdrProfileDescriptor();
        profileDescriptor.setProfileName(profileName);
        profileDescriptor.setSelected(true);
        profileDescriptor.getContents().add(this.getDefaultProgressBar());
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
        groupDescriptor.setSize(new Dimension(240,30));
        groupDescriptor.setContentType(AdrTrackerGroupContentType.PROGRESS_BARS);
        return groupDescriptor;
    }
    private AdrTrackerGroupDescriptor getDefaultGroup(){
        AdrTrackerGroupDescriptor groupDescriptor = new AdrTrackerGroupDescriptor();
        groupDescriptor.setTitle("group");
        groupDescriptor.setType(AdrComponentType.TRACKER_GROUP);
        groupDescriptor.setOrientation(AdrComponentOrientation.VERTICAL);
        return groupDescriptor;
    }
    private AdrProfileDescriptor getShowcaseProfile(){
        AdrProfileDescriptor profile = new AdrProfileDescriptor();
        profile.setProfileName("Showcase");
        profile.setContents(new ArrayList<>());
        profile.setSelected(false);

        HotKeyDescriptor hotKey = new HotKeyDescriptor(50, '2', false, false, false, false);

        AdrTrackerGroupDescriptor staticIconH = this.getDefaultIconGroup();
        AdrTrackerGroupDescriptor staticIconV = this.getDefaultIconGroup();
        AdrTrackerGroupDescriptor dynamicIconV = this.getDefaultIconGroup();
        AdrTrackerGroupDescriptor dynamicIconH = this.getDefaultIconGroup();

        AdrTrackerGroupDescriptor staticPBH = this.getDefaultPBGroup();
        AdrTrackerGroupDescriptor staticBiggerPBH = this.getDefaultPBGroup();
        AdrTrackerGroupDescriptor staticPBV = this.getDefaultPBGroup();
        AdrTrackerGroupDescriptor dynamicPBV = this.getDefaultPBGroup();
        AdrTrackerGroupDescriptor dynamicPBH = this.getDefaultPBGroup();

        staticBiggerPBH.setSize(new Dimension(300,46));

        staticPBV.setVGap(2);
        staticBiggerPBH.setVGap(1);

        staticIconH.setLocation(new Point(424,300));
        staticIconV.setLocation(new Point(488,366));
        staticPBV.setLocation(new Point(554,366));
        staticBiggerPBH.setLocation(new Point(554,462));

        staticIconH.setTitle("Horizontal icon group");
        staticIconV.setTitle("Vertical icon group");
        staticPBV.setTitle("Horizontal PB group");
        staticBiggerPBH.setTitle("Bigger VPB group");

        staticIconH.setOrientation(AdrComponentOrientation.HORIZONTAL);
        staticIconV.setOrientation(AdrComponentOrientation.VERTICAL);
        staticPBV.setOrientation(AdrComponentOrientation.VERTICAL);
        staticBiggerPBH.setOrientation(AdrComponentOrientation.VERTICAL);

        staticIconH.getCells().clear();
        staticIconV.getCells().clear();
        dynamicIconV.getCells().clear();
        dynamicIconH.getCells().clear();
        staticPBH.getCells().clear();
        staticPBV.getCells().clear();
        staticBiggerPBH.getCells().clear();
        dynamicPBH.getCells().clear();
        dynamicPBV.getCells().clear();

        AdrIconDescriptor icon1 = this.getDefaultIcon();
        icon1.setIconPath("Bismuth_Flask_status_icon.png");
        icon1.setHotKeyDescriptor(hotKey);
        icon1.setDuration(3d);
        AdrIconDescriptor icon2 = this.getDefaultIcon();
        icon2.setIconPath("Vaal_Lightning_Strike_skill_icon.png");
        icon2.setDuration(5d);
        icon2.setHotKeyDescriptor(hotKey);
        AdrIconDescriptor icon3 = this.getDefaultIcon();
        icon3.setIconPath("Silver_Flask_status_icon.png");
        icon3.setDuration(8d);
        icon3.setHotKeyDescriptor(hotKey);

        staticIconH.getCells().add(icon1);
        staticIconH.getCells().add(icon2);
        staticIconH.getCells().add(icon3);

        AdrIconDescriptor icon4 = this.getDefaultIcon();
        icon4.setIconPath("Arctic_Armour_skill_icon.png");
        icon4.setHotKeyDescriptor(hotKey);
        icon4.setDuration(3d);
        icon4.setAlwaysVisible(true);
        AdrIconDescriptor icon5 = this.getDefaultIcon();
        icon5.setIconPath("Phase_Run_skill_icon.png");
        icon5.setDuration(5d);
        icon5.setHotKeyDescriptor(hotKey);
        AdrIconDescriptor icon6 = this.getDefaultIcon();
        icon6.setIconPath("Arctic_Armour_skill_icon.png");
        icon6.setDuration(8d);
        icon6.setHotKeyDescriptor(hotKey);
        icon6.setAlwaysVisible(true);

        AdrProgressBarDescriptor pb1 = this.getDefaultProgressBar();
        pb1.setIconPath("Topaz_Flask_status_icon.png");
        pb1.setHotKeyDescriptor(hotKey);
        pb1.setTextFormat("0");
        pb1.setDuration(3d);
        AdrProgressBarDescriptor pb2 = this.getDefaultProgressBar();
        pb2.setIconPath("Quicksilver_Flask_status_icon.png");
        pb2.setIconAlignment(AdrIconAlignment.RIGHT);
        pb2.setBackgroundColor(new Color(47,52,59));
        pb2.setForegroundColor(new Color(227,205,164));
        pb2.setHotKeyDescriptor(hotKey);
        pb2.setTextFormat("0.0");
        pb2.setBindToTextColor(true);
        pb2.setDuration(6d);
        AdrProgressBarDescriptor pb3 = this.getDefaultProgressBar();
        pb3.setIconEnable(false);
        pb3.setHotKeyDescriptor(hotKey);
        pb3.setFontSize(22);
        pb3.setBackgroundColor(new Color(47,52,59));
        pb3.setForegroundColor(new Color(126,130,122));
        pb3.setBorderColor(new Color(126,130,122));
        pb3.setTextFormat("0.00");
        pb3.setBindToTextColor(false);
        pb3.setDuration(10d);

        AdrProgressBarDescriptor pb4 = this.getDefaultProgressBar();
        pb4.setIconPath("Stibnite_Flask_status_icon.png");
        pb4.setIconAlignment(AdrIconAlignment.RIGHT);
        pb4.setHotKeyDescriptor(hotKey);
        pb4.setBackgroundColor(new Color(47,52,59));
        pb4.setForegroundColor(new Color(126,130,122));
        pb4.setBorderColor(new Color(126,130,122));
        pb4.setTextFormat("0.0");
        pb4.setDuration(8d);
        AdrProgressBarDescriptor pb5 = this.getDefaultProgressBar();
        pb5.setIconPath("Granite_Flask_status_icon.png");
        pb5.setIconAlignment(AdrIconAlignment.LEFT);
        pb5.setHotKeyDescriptor(hotKey);
        pb5.setFontSize(22);
        pb5.setDefaultValueTextColor(new Color(47,52,59));
        pb5.setBackgroundColor(new Color(167,163,126));
        pb5.setForegroundColor(new Color(239,236,202));
        pb5.setBorderColor(new Color(230,226,175));
        pb5.setTextFormat("0.0");
        pb5.setBindToTextColor(false);
        pb5.setDuration(20d);

        AdrIconDescriptor icon7 = this.getDefaultIcon();
        icon7.setIconPath("Arctic_Armour_skill_icon.png");
        icon7.setDuration(8d);
        icon7.setHotKeyDescriptor(hotKey);
        icon7.setAlwaysVisible(true);

        staticIconV.getCells().add(icon4);
        staticIconV.getCells().add(icon5);
        staticIconV.getCells().add(icon6);

        staticPBV.getCells().add(pb1);
        staticPBV.getCells().add(pb2);
        staticPBV.getCells().add(pb3);

        staticBiggerPBH.getCells().add(pb4);
        staticBiggerPBH.getCells().add(pb5);

        profile.getContents().add(staticIconH);
        profile.getContents().add(staticIconV);
        profile.getContents().add(staticPBV);
        profile.getContents().add(staticBiggerPBH);
        profile.getContents().add(icon7);
        return profile;
    }
}
