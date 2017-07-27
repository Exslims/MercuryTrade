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
    private AtomicInteger idGenerator = new AtomicInteger();
    public AdrConfigurationServiceMock(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public List<AdrProfileDescriptor> getEntities() {
        return this.getDefault();
    }


    @Override
    public List<AdrProfileDescriptor> getDefault() {
        return Arrays.stream(new AdrProfileDescriptor[]{this.getShowcaseProfile()}).collect(Collectors.toList());
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setAdrProfileDescriptorList(this.getDefault());
    }

    @Override
    public void validate() {
        if(this.selectedProfile.getAdrProfileDescriptorList() == null){
            this.selectedProfile.setAdrProfileDescriptorList(this.getDefault());
        }
    }

    @Override
    public AdrIconDescriptor getDefaultIcon() {
        AdrIconDescriptor icon = new AdrIconDescriptor();
        icon.setId(this.idGenerator.incrementAndGet());
        icon.setTitle("icon");
        icon.setIconPath("default_icon");
        icon.setLocation(new Point(new Random().nextInt(600), new Random().nextInt(600)));
        icon.setSize(new Dimension(64, 64));
        icon.setDuration(6.0d);
        icon.setHotKeyDescriptor(new HotKeyDescriptor());
        icon.setIconType(AdrIconType.SQUARE);
        icon.setOrientation(AdrComponentOrientation.HORIZONTAL);
        icon.setType(AdrComponentType.ICON);
        icon.setDefaultValueTextColor(new Color(255,250,213));
        icon.setMediumValueTextColor(new Color(255,211,78));
        icon.setLowValueTextColor(new Color(224,86,60));
        icon.setBorderColor(new Color(16,110,99));
        icon.setLowValueTextThreshold(1.0);
        icon.setMediumValueTextThreshold(3.0);
        icon.setDefaultValueTextThreshold(5.0);
        icon.setTextFormat("0.0");
        return icon;
    }

    @Override
    public AdrProgressBarDescriptor getDefaultProgressBar() {
        AdrProgressBarDescriptor progressBar = new AdrProgressBarDescriptor();
        progressBar.setId(this.idGenerator.incrementAndGet());
        progressBar.setTitle("progress bar");
        progressBar.setIconPath("default_icon");
        progressBar.setLocation(new Point(new Random().nextInt(600), new Random().nextInt(600)));
        progressBar.setSize(new Dimension(240, 30));
        progressBar.setDuration(6.0d);
        progressBar.setOrientation(AdrComponentOrientation.HORIZONTAL);
        progressBar.setHotKeyDescriptor(new HotKeyDescriptor(50,'2',false,false,false,false));
        progressBar.setType(AdrComponentType.PROGRESS_BAR);
        progressBar.setDefaultValueTextColor(new Color(255,250,213));
        progressBar.setMediumValueTextColor(new Color(255,211,78));
        progressBar.setLowValueTextColor(new Color(224,86,60));
        progressBar.setBackgroundColor(new Color(59, 59, 59));
        progressBar.setForegroundColor(new Color(16,91,99));
        progressBar.setBorderColor(new Color(16,110,99));
        progressBar.setIconAlignment(AdrIconAlignment.LEFT);
        progressBar.setLowValueTextThreshold(1.0);
        progressBar.setMediumValueTextThreshold(3.0);
        progressBar.setDefaultValueTextThreshold(5.0);
        progressBar.setThickness(1);
        progressBar.setTextFormat("0.0");
        return progressBar;
    }

    @Override
    public AdrTrackerGroupDescriptor getDefaultIconGroup() {
        AdrTrackerGroupDescriptor groupDescriptor = this.getDefaultGroup();
        groupDescriptor.setTitle("icon group");
        groupDescriptor.setSize(new Dimension(64,64));
        groupDescriptor.setContentType(AdrTrackerGroupContentType.ICONS);
        List<AdrComponentDescriptor> icons = new ArrayList<>();
        groupDescriptor.setCells(icons);
        return groupDescriptor;
    }

    @Override
    public AdrTrackerGroupDescriptor getDefaultPBGroup() {
        AdrTrackerGroupDescriptor groupDescriptor = this.getDefaultGroup();
        groupDescriptor.setTitle("progress bar group");
        groupDescriptor.setSize(new Dimension(240,30));
        groupDescriptor.setContentType(AdrTrackerGroupContentType.PROGRESS_BARS);
        List<AdrComponentDescriptor> pbList = new ArrayList<>();
        groupDescriptor.setCells(pbList);
        return groupDescriptor;
    }
    private AdrTrackerGroupDescriptor getDefaultGroup(){
        AdrTrackerGroupDescriptor groupDescriptor = new AdrTrackerGroupDescriptor();
        groupDescriptor.setId(this.idGenerator.incrementAndGet());
        groupDescriptor.setTitle("group");
        groupDescriptor.setLocation(new Point(new Random().nextInt(600), new Random().nextInt(600)));
        groupDescriptor.setType(AdrComponentType.GROUP);
        groupDescriptor.setOrientation(AdrComponentOrientation.VERTICAL);
        groupDescriptor.setGroupType(AdrTrackerGroupType.STATIC);
        return groupDescriptor;
    }
    private AdrProfileDescriptor getShowcaseProfile(){
        AdrProfileDescriptor profile = new AdrProfileDescriptor();
        profile.setProfileName("Showcase");
        profile.setContents(new ArrayList<>());
        profile.setSelected(true);

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
        icon1.setIconPath("Bismuth_Flask");
        icon1.setHotKeyDescriptor(hotKey);
        icon1.setDuration(3d);
        AdrIconDescriptor icon2 = this.getDefaultIcon();
        icon2.setIconPath("Bleeding_Immunity");
        icon2.setDuration(5d);
        icon2.setHotKeyDescriptor(hotKey);
        AdrIconDescriptor icon3 = this.getDefaultIcon();
        icon3.setIconPath("Silver_Flask");
        icon3.setDuration(8d);
        icon3.setHotKeyDescriptor(hotKey);

        staticIconH.getCells().add(icon1);
        staticIconH.getCells().add(icon2);
        staticIconH.getCells().add(icon3);

        AdrIconDescriptor icon4 = this.getDefaultIcon();
        icon4.setIconPath("Arctic_Armour_skill_icon");
        icon4.setHotKeyDescriptor(hotKey);
        icon4.setDuration(3d);
        AdrIconDescriptor icon5 = this.getDefaultIcon();
        icon5.setIconPath("Phase_Run_skill_icon");
        icon5.setDuration(5d);
        icon5.setHotKeyDescriptor(hotKey);
        AdrIconDescriptor icon6 = this.getDefaultIcon();
        icon6.setIconPath("Arctic_Armour_skill_icon");
        icon6.setDuration(8d);
        icon6.setHotKeyDescriptor(hotKey);

        AdrProgressBarDescriptor pb1 = this.getDefaultProgressBar();
        pb1.setIconPath("Topaz_flask");
        pb1.setHotKeyDescriptor(hotKey);
        pb1.setTextFormat("0");
        pb1.setDuration(3d);
        AdrProgressBarDescriptor pb2 = this.getDefaultProgressBar();
        pb2.setIconPath("Quicksilver_Flask");
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
        pb4.setIconPath("Stibnite_Flask");
        pb4.setIconAlignment(AdrIconAlignment.RIGHT);
        pb4.setHotKeyDescriptor(hotKey);
        pb4.setBackgroundColor(new Color(47,52,59));
        pb4.setForegroundColor(new Color(126,130,122));
        pb4.setBorderColor(new Color(126,130,122));
        pb4.setTextFormat("0.0");
        pb4.setDuration(8d);
        AdrProgressBarDescriptor pb5 = this.getDefaultProgressBar();
        pb5.setIconPath("Granite_Flask");
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
        return profile;
    }
}
