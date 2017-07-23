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
import java.util.stream.Collectors;

public class AdrConfigurationServiceMock extends BaseConfigurationService<List<AdrProfileDescriptor>> implements AdrConfigurationService {
    public AdrConfigurationServiceMock(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public List<AdrProfileDescriptor> getEntities() {
        return this.getDefault();
    }


    @Override
    public List<AdrProfileDescriptor> getDefault() {
        AdrProfileDescriptor profile = new AdrProfileDescriptor();
        profile.setSelected(true);
        AdrGroupDescriptor groupDescriptor = new AdrGroupDescriptor();
        groupDescriptor.setSize(new Dimension(64,64));
        groupDescriptor.setLocation(new Point(400,400));
        groupDescriptor.setTitle("icon group");
        groupDescriptor.setGroupType(AdrGroupType.STATIC);
        groupDescriptor.setContentType(AdrGroupContentType.ICONS);
        groupDescriptor.setOrientation(AdrComponentOrientation.VERTICAL);
        groupDescriptor.setType(AdrComponentType.GROUP);

        AdrIconDescriptor icon1 = this.getDefaultIcon();
        icon1.setIconPath("Arctic_Armour_skill_icon");
        icon1.setHotKeyDescriptor(new HotKeyDescriptor(49,'1',false,false,false,false));
        icon1.setDuration(3d);
        AdrIconDescriptor icon2 = this.getDefaultIcon();
        icon2.setIconPath("Blood_Rage_skill_icon");
        icon2.setDuration(5d);
        icon2.setHotKeyDescriptor(new HotKeyDescriptor(50,'2',false,false,false,false));
        AdrIconDescriptor icon3 = this.getDefaultIcon();
        icon3.setIconPath("Bismuth_Flask");
        icon3.setDuration(8d);
        icon3.setHotKeyDescriptor(new HotKeyDescriptor(51,'3',false,true,false,false));
        groupDescriptor.setCells(Arrays.asList(icon1,icon2,icon3,icon2));

        AdrGroupDescriptor groupDescriptor1 = new AdrGroupDescriptor();
        groupDescriptor1.setSize(new Dimension(64,64));
        groupDescriptor1.setLocation(new Point(500,500));
        groupDescriptor1.setType(AdrComponentType.GROUP);
        groupDescriptor1.setContentType(AdrGroupContentType.ICONS);
        groupDescriptor1.setOrientation(AdrComponentOrientation.VERTICAL);
        groupDescriptor1.setGroupType(AdrGroupType.DYNAMIC);
        groupDescriptor1.setCells(Arrays.asList(icon3,icon1,icon2,icon3));
        profile.setContents(Arrays.stream(new AdrComponentDescriptor[] {groupDescriptor,groupDescriptor1,this.getDefaultPBGroup()}).collect(Collectors.toList()));
        return Arrays.stream(new AdrProfileDescriptor[]{profile}).collect(Collectors.toList());
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
        icon.setTitle("icon");
        icon.setIconPath("default_icon");
        icon.setLocation(new Point(new Random().nextInt(600), new Random().nextInt(600)));
        icon.setSize(new Dimension(64, 64));
        icon.setDuration(0d);
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
        progressBar.setTitle("progress bar");
        progressBar.setIconPath("Arctic_Armour_skill_icon");
        progressBar.setLocation(new Point(new Random().nextInt(600), new Random().nextInt(600)));
        progressBar.setSize(new Dimension(240, 30));
        progressBar.setDuration(6.56d);
        progressBar.setOrientation(AdrComponentOrientation.HORIZONTAL);
        progressBar.setHotKeyDescriptor(new HotKeyDescriptor(50,'2',false,false,false,false));
        progressBar.setType(AdrComponentType.PROGRESS_BAR);
        progressBar.setDefaultValueTextColor(new Color(255,250,213));
        progressBar.setMediumValueTextColor(new Color(255,211,78));
        progressBar.setLowValueTextColor(new Color(224,86,60));
        progressBar.setBackgroundColor(new Color(59, 59, 59));
        progressBar.setForegroundColor(new Color(16,91,99));
        progressBar.setBorderColor(new Color(16,110,99));
        progressBar.setLowValueTextThreshold(1.0);
        progressBar.setMediumValueTextThreshold(3.0);
        progressBar.setDefaultValueTextThreshold(5.0);
        progressBar.setThickness(1);
        progressBar.setTextFormat("0.0");
        return progressBar;
    }

    @Override
    public AdrGroupDescriptor getDefaultIconGroup() {
        AdrGroupDescriptor groupDescriptor = this.getDefaultGroup();
        groupDescriptor.setTitle("icon group");
        groupDescriptor.setSize(new Dimension(64,64));
        groupDescriptor.setContentType(AdrGroupContentType.ICONS);
        List<AdrComponentDescriptor> icons = new ArrayList<>();
        icons.add(this.getDefaultIcon());
        groupDescriptor.setCells(icons);
        return groupDescriptor;
    }

    @Override
    public AdrGroupDescriptor getDefaultPBGroup() {
        AdrGroupDescriptor groupDescriptor = this.getDefaultGroup();
        groupDescriptor.setTitle("progress bar group");
        groupDescriptor.setSize(new Dimension(240,30));
        groupDescriptor.setContentType(AdrGroupContentType.PROGRESS_BARS);
        List<AdrComponentDescriptor> pbList = new ArrayList<>();
        pbList.add(this.getDefaultProgressBar());
        pbList.add(this.getDefaultProgressBar());
        pbList.add(this.getDefaultProgressBar());
        groupDescriptor.setCells(pbList);
        return groupDescriptor;
    }
    private AdrGroupDescriptor getDefaultGroup(){
        AdrGroupDescriptor groupDescriptor = new AdrGroupDescriptor();
        groupDescriptor.setTitle("group");
        groupDescriptor.setLocation(new Point(new Random().nextInt(600), new Random().nextInt(600)));
        groupDescriptor.setType(AdrComponentType.GROUP);
        groupDescriptor.setOrientation(AdrComponentOrientation.VERTICAL);
        groupDescriptor.setGroupType(AdrGroupType.STATIC);
        return groupDescriptor;
    }
}
