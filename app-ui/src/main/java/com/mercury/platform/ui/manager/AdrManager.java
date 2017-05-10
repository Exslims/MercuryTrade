package com.mercury.platform.ui.manager;


import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.entity.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.entity.adr.AdrProfile;
import com.mercury.platform.ui.frame.adr.components.AbstractAdrFrame;
import com.mercury.platform.ui.frame.adr.AdrManagerFrame;
import com.mercury.platform.ui.frame.adr.components.AdrGroupFrame;
import com.mercury.platform.ui.frame.setup.adr.AdrState;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AdrManager {
    private List<AbstractAdrFrame> frames = new ArrayList<>();
    private AdrManagerFrame adrManagerFrame;
    @Getter
    private AdrState state = AdrState.DEFAULT;
    public void load(){
        this.adrManagerFrame = new AdrManagerFrame();
        List<AdrProfile> entities = Configuration.get().adrGroupConfiguration().getEntities();
        entities.get(0).getContents().forEach(it -> frames.add(new AdrGroupFrame((AdrGroupDescriptor)it)));

        this.frames.forEach(it -> {
            it.init();
            it.disableSettings();
        });
        this.adrManagerFrame.init();
    }
    public void enableSettings(){
        state = AdrState.SETTINGS;
        adrManagerFrame.showComponent();
        frames.forEach(AbstractAdrFrame::enableSettings);
    }
    public void disableSettings(){
        state = AdrState.DEFAULT;
        adrManagerFrame.hideComponent();
        frames.forEach(AbstractAdrFrame::disableSettings);
    }
}
