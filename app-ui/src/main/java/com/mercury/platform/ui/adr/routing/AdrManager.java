package com.mercury.platform.ui.adr.routing;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.ListConfigurationService;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.ui.adr.components.AbstractAdrFrame;
import com.mercury.platform.ui.adr.AdrManagerFrame;
import com.mercury.platform.ui.adr.components.AdrGroupFrame;
import com.mercury.platform.ui.adr.AdrState;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AdrManager implements AsSubscriber{
    private List<AbstractAdrFrame> frames = new ArrayList<>();
    private AdrProfileDescriptor selectedProfile;
    private ListConfigurationService<AdrProfileDescriptor> config;
    private AdrManagerFrame adrManagerFrame;
    @Getter
    private AdrState state = AdrState.DEFAULT;
    public void load(){
        this.config = Configuration.get().adrGroupConfiguration();
        this.selectedProfile = this.config.getEntities()
                .stream()
                .filter(AdrProfileDescriptor::isSelected)
                .findAny().orElse(null);
        this.adrManagerFrame = new AdrManagerFrame(this.selectedProfile);
        this.selectedProfile.getContents().forEach(component -> {
            switch (component.getType()){
                case GROUP: {
                    this.frames.add(new AdrGroupFrame((AdrGroupDescriptor) component));
                    break;
                }
            }
        });

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

    @Override
    public void subscribe() {
        MercuryStoreUI.adrSelectSubject.subscribe(component -> {

        });
        MercuryStoreUI.adrSelectProfileSubject.subscribe(profile -> {
            this.selectedProfile.setSelected(false);
            this.selectedProfile = profile;
            //todo
            profile.setSelected(true);
        });
    }
}
