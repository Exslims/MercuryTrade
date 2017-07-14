package com.mercury.platform.shared.config.configration.impl.adr;


import com.mercury.platform.shared.config.configration.AdrConfigurationService;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
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
        groupDescriptor.setTitle("qweqweqweqweqwe");
        groupDescriptor.setGroupType(AdrGroupType.STATIC);
        groupDescriptor.setContentType(AdrGroupContentType.ICONS);
        groupDescriptor.setDirection(AdrComponentDirection.VERTICAL);
        groupDescriptor.setType(AdrComponentType.GROUP);

        AdrIconDescriptor icon1 = new AdrIconDescriptor();
        icon1.setIconPath("Arctic_Armour_skill_icon");
        icon1.setLocation(new Point(400, 400));
        icon1.setTitle("qweqweqweqweqweqwe");
        icon1.setSize(new Dimension(64, 64));
        icon1.setDuration(8f);
        icon1.setIconType(AdrIconType.SQUARE);
        icon1.setType(AdrComponentType.ICON);
        AdrIconDescriptor icon2 = new AdrIconDescriptor();
        icon2.setIconPath("Blood_Rage_skill_icon");
        icon2.setLocation(new Point(400, 400));
        icon2.setSize(new Dimension(64, 64));
        icon2.setDuration(8f);
        icon2.setIconType(AdrIconType.SQUARE);
        icon2.setType(AdrComponentType.ICON);
        AdrIconDescriptor icon3 = new AdrIconDescriptor();
        icon3.setIconPath("Bismuth_Flask");
        icon3.setLocation(new Point(400, 400));
        icon3.setSize(new Dimension(64, 64));
        icon3.setDuration(8f);
        icon3.setIconType(AdrIconType.ELIPSE);
        icon3.setType(AdrComponentType.ICON);
        groupDescriptor.setCells(Arrays.asList(icon1,icon2,icon3,icon2));

        AdrGroupDescriptor groupDescriptor1 = new AdrGroupDescriptor();
        groupDescriptor1.setSize(new Dimension(64,64));
        groupDescriptor1.setLocation(new Point(500,500));
        groupDescriptor1.setType(AdrComponentType.GROUP);
        groupDescriptor1.setContentType(AdrGroupContentType.ICONS);
        groupDescriptor1.setDirection(AdrComponentDirection.VERTICAL);
        groupDescriptor1.setGroupType(AdrGroupType.DYNAMIC);
        groupDescriptor1.setCells(Arrays.asList(icon3,icon1,icon2,icon3));
        profile.setContents(Arrays.stream(new AdrComponentDescriptor[] {groupDescriptor,this.getDefaultPBGroup(),groupDescriptor1, icon1,groupDescriptor,icon3,this.getDefaultProgressBar()}).collect(Collectors.toList()));
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
        icon.setDuration(0f);
        icon.setIconType(AdrIconType.SQUARE);
        icon.setType(AdrComponentType.ICON);
        return icon;
    }

    @Override
    public AdrProgressBarDescriptor getDefaultProgressBar() {
        AdrProgressBarDescriptor progressBar = new AdrProgressBarDescriptor();
        progressBar.setTitle("progress bar");
        progressBar.setIconPath("default_progress_bar_icon");
        progressBar.setLocation(new Point(new Random().nextInt(600), new Random().nextInt(600)));
        progressBar.setSize(new Dimension(240, 64));
        progressBar.setDuration(0f);
        progressBar.setType(AdrComponentType.PROGRESS_BAR);
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
        groupDescriptor.setSize(new Dimension(240,64));
        groupDescriptor.setContentType(AdrGroupContentType.PROGRESS_BARS);
        List<AdrComponentDescriptor> pbList = new ArrayList<>();
        pbList.add(this.getDefaultProgressBar());
        groupDescriptor.setCells(pbList);
        return groupDescriptor;
    }
    private AdrGroupDescriptor getDefaultGroup(){
        AdrGroupDescriptor groupDescriptor = new AdrGroupDescriptor();
        groupDescriptor.setTitle("group");
        groupDescriptor.setLocation(new Point(new Random().nextInt(600), new Random().nextInt(600)));
        groupDescriptor.setType(AdrComponentType.GROUP);
        groupDescriptor.setDirection(AdrComponentDirection.VERTICAL);
        groupDescriptor.setGroupType(AdrGroupType.STATIC);
        return groupDescriptor;
    }
}
