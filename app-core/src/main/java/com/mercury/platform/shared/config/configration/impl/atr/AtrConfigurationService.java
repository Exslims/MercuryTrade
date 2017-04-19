package com.mercury.platform.shared.config.configration.impl.atr;


import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.entity.atr.AtrGroupSettings;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class AtrConfigurationService extends BaseConfigurationService implements ListConfigurationService<AtrGroupSettings>{
    public AtrConfigurationService(ConfigurationSource dataSource) {
        super(dataSource);
    }
    @Override
    public List<AtrGroupSettings> getEntities() {
        return null;
    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public void save() {

    }

    @Override
    public AtrGroupSettings getDefault() {
        return new AtrGroupSettings(
                new FrameSettings(new Point(500,500),new Dimension(90,400)),1f,1f);
    }
}
