package com.mercury.platform.shared.config.configration.impl.adr;


import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.entity.adr.AdrCellDescriptor;
import com.mercury.platform.shared.entity.adr.AdrGroupDescriptor;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdrConfigurationService extends BaseConfigurationService implements ListConfigurationService<AdrGroupDescriptor>{
    public AdrConfigurationService(ConfigurationSource dataSource) {
        super(dataSource);
    }
    @Override
    public List<AdrGroupDescriptor> getEntities() {

        return Arrays.asList(
                new AdrGroupDescriptor(
                        new Point(500,500),
                        Arrays.asList(
                                new AdrCellDescriptor("Arctic_Armour_skill_icon",new Dimension(64,64),8f),
                                new AdrCellDescriptor("Blood_Rage_skill_icon",new Dimension(64,64),5f),
                                new AdrCellDescriptor("Phase_Run_skill_icon",new Dimension(64,64),6f),
                                new AdrCellDescriptor("Granite_Flask",new Dimension(64,64),4f)),
                        1f,1f)
        );
    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public void save() {

    }

    @Override
    public AdrGroupDescriptor getDefault() {
        return new AdrGroupDescriptor(
                new Point(500,500),Arrays.asList(new AdrCellDescriptor()),1f,1f);
    }
}
