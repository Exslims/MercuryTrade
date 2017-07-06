package com.mercury.platform.shared.config.configration.impl.adr;


import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentWrapper;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.entity.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AdrConfigurationServiceMock extends BaseConfigurationService<List<AdrProfileDescriptor>> implements ListConfigurationService<AdrProfileDescriptor>{
    public AdrConfigurationServiceMock(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public List<AdrProfileDescriptor> getEntities() {
        return this.selectedProfile.getAdrProfileDescriptorList();
    }


    @Override
    public List<AdrProfileDescriptor> getDefault() {
        AdrProfileDescriptor profile = new AdrProfileDescriptor();
        AdrGroupDescriptor groupDescriptor = new AdrGroupDescriptor(new Dimension(64,64), new Point(400,400));
        groupDescriptor.setCells(Arrays.asList(
                new AdrIconDescriptor("Arctic_Armour_skill_icon",new Point(400,400),new Dimension(64,64),8f),
                new AdrIconDescriptor("Blood_Rage_skill_icon",new Point(400,400),new Dimension(64,64),5f),
                new AdrIconDescriptor("Phase_Run_skill_icon",new Point(400,400),new Dimension(64,64),6f),
                new AdrIconDescriptor("Granite_Flask",new Point(400,400),new Dimension(64,64),4f)));

        AdrGroupDescriptor groupDescriptor1 = new AdrGroupDescriptor(new Dimension(64,64), new Point(500,500));;
        groupDescriptor1.setCells(Arrays.asList(
                new AdrIconDescriptor("Bismuth_Flask",new Point(400,400),new Dimension(64,64),8f),
                new AdrIconDescriptor("Silver_Flask",new Point(400,400),new Dimension(64,64),5f),
                new AdrIconDescriptor("Stibnite_Flask",new Point(400,400),new Dimension(64,64),6f),
                new AdrIconDescriptor("Granite_Flask",new Point(400,400),new Dimension(64,64),4f)));
        profile.setContents(Arrays.asList(
                new AdrComponentWrapper(AdrComponentType.GROUP,groupDescriptor),
                new AdrComponentWrapper(AdrComponentType.GROUP,groupDescriptor1),
                new AdrComponentWrapper(AdrComponentType.ICONIZED,new AdrIconDescriptor("Granite_Flask",new Point(400,400),new Dimension(64,64),4f))));

        return Arrays.asList(profile);
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
}
