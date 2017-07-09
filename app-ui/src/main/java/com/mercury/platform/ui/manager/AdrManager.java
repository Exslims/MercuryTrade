package com.mercury.platform.ui.manager;


import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.ui.frame.adr.components.AbstractAdrFrame;
import com.mercury.platform.ui.frame.adr.AdrManagerFrame;
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
        List<AdrProfileDescriptor> entities = Configuration.get().adrGroupConfiguration().getEntities();
//        entities.get(0).getContents().forEach(it -> frames.add(new AdrGroupFrame((AdrGroupDescriptor) it.getComponentDescriptor())));

        this.frames.forEach(it -> {
            it.init();
            it.disableSettings();
        });
        this.adrManagerFrame.init();
    }
    public void enableSettings(){
        this.state = AdrState.SETTINGS;
        this.adrManagerFrame.showComponent();
        this.frames.forEach(AbstractAdrFrame::enableSettings);
    }
    public void disableSettings(){
        this.state = AdrState.DEFAULT;
        this.adrManagerFrame.hideComponent();
        this.frames.forEach(AbstractAdrFrame::disableSettings);
    }
}
