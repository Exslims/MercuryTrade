package com.mercury.platform.shared.config.configration.impl.adr;


import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.entity.FrameSettings;
import com.mercury.platform.shared.entity.adr.AdrGroupSettings;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class AdrConfigurationService extends BaseConfigurationService implements ListConfigurationService<AdrGroupSettings>{
    public AdrConfigurationService(ConfigurationSource dataSource) {
        super(dataSource);
    }
    @Override
    public List<AdrGroupSettings> getEntities() {
        return null;
    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public void save() {

    }

    @Override
    public AdrGroupSettings getDefault() {
        return new AdrGroupSettings(
                new FrameSettings(new Point(500,500),new Dimension(90,400)),1f,1f);
    }
}
