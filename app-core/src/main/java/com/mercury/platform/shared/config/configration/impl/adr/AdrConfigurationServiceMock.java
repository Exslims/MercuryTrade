package com.mercury.platform.shared.config.configration.impl.adr;


import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.entity.adr.AdrIconDescriptor;
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
        AdrProfileDescriptor profile = new AdrProfileDescriptor();
        AdrGroupDescriptor groupDescriptor = new AdrGroupDescriptor();
        groupDescriptor.setCells(Arrays.asList(
                new AdrIconDescriptor("Arctic_Armour_skill_icon",new Point(400,400),new Dimension(64,64),8f),
                new AdrIconDescriptor("Blood_Rage_skill_icon",new Point(400,400),new Dimension(64,64),5f),
                new AdrIconDescriptor("Phase_Run_skill_icon",new Point(400,400),new Dimension(64,64),6f),
                new AdrIconDescriptor("Granite_Flask",new Point(400,400),new Dimension(64,64),4f)));

        AdrGroupDescriptor groupDescriptor1 = new AdrGroupDescriptor();
        groupDescriptor1.setCells(Arrays.asList(
                new AdrIconDescriptor("Bismuth_Flask",new Point(400,400),new Dimension(64,64),8f),
                new AdrIconDescriptor("Silver_Flask",new Point(400,400),new Dimension(64,64),5f),
                new AdrIconDescriptor("Stibnite_Flask",new Point(400,400),new Dimension(64,64),6f),
                new AdrIconDescriptor("Granite_Flask",new Point(400,400),new Dimension(64,64),4f)));
        profile.setContents(Arrays.asList(groupDescriptor,groupDescriptor1));

        return Arrays.asList(profile);
    }


    @Override
    public List<AdrProfileDescriptor> getDefault() {
        return null;
    }

    @Override
    public void toDefault() {

    }

    @Override
    public void validate() {

    }
}
