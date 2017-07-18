package com.mercury.platform.ui.adr;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.AdrConfigurationService;
import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.adr.components.AbstractAdrFrame;
import com.mercury.platform.ui.adr.components.AdrManagerFrame;
import com.mercury.platform.ui.adr.components.AdrGroupFrame;
import com.mercury.platform.ui.adr.components.panel.page.*;
import com.mercury.platform.ui.adr.routing.AdrPageDefinition;
import com.mercury.platform.ui.adr.routing.AdrPageState;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AdrManager implements AsSubscriber{
    private List<AbstractAdrFrame> frames = new ArrayList<>();
    private AdrProfileDescriptor selectedProfile;
    private AdrConfigurationService config;
    private AdrManagerFrame adrManagerFrame;

    //pages
    private AdrPagePanel<AdrGroupDescriptor> groupSettingsPanel;
    private AdrPagePanel<AdrComponentDescriptor> mainPanel;
    private AdrPagePanel<AdrIconDescriptor> iconSettingsPanel;
    private AdrPagePanel<AdrProgressBarDescriptor> progressBarSettingsPanel;
    private AdrPagePanel<List<AdrProfileDescriptor>> profilesSettingsPanel;
    @Getter
    private AdrState state = AdrState.DEFAULT;
    public void load(){
        this.groupSettingsPanel = new AdrGroupPagePanel();
        this.mainPanel = new AdrMainPagePanel();
        this.iconSettingsPanel = new AdrIconPagePanel();
        this.progressBarSettingsPanel = new AdrProgressBarPagePanel();
        this.profilesSettingsPanel = new AdrProfilePagePanel();

        this.config = Configuration.get().adrConfiguration();
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
        this.subscribe();

        this.mainPanel.setPayload(null);
        this.adrManagerFrame.setPage(this.mainPanel);
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
    @SuppressWarnings("all")
    public void subscribe() {
        MercuryStoreUI.adrStateSubject.subscribe(definition -> {
            switch (definition.getState()){
                case MAIN: {
                    this.mainPanel.setPayload((AdrComponentDescriptor) definition.getPayload());
                    this.adrManagerFrame.setPage(this.mainPanel);
                    break;
                }
                case PROFILE: {
                    this.profilesSettingsPanel.setPayload((List<AdrProfileDescriptor>) definition.getPayload());
                    this.adrManagerFrame.setPage(this.profilesSettingsPanel);
                    break;
                }
            }
        });
        MercuryStoreUI.adrComponentStateSubject.subscribe(definition -> {
           switch (definition.getOperations()){
               case NEW_COMPONENT:{
                   if(definition.getDescriptor() instanceof AdrGroupDescriptor){
                       AdrGroupDescriptor defaultGroup = this.config.getDefaultIconGroup();
                       this.selectedProfile.getContents().add(defaultGroup);
                       AdrGroupFrame adrGroupFrame = new AdrGroupFrame(defaultGroup);
                       adrGroupFrame.init();
                       adrGroupFrame.showComponent();
                       adrGroupFrame.enableSettings();
                       this.frames.add(adrGroupFrame);
                       this.groupSettingsPanel.setPayload(defaultGroup);
                       this.adrManagerFrame.reloadTree();
                       this.adrManagerFrame.setPage(this.groupSettingsPanel);
                   }
                   break;
               }
               case EDIT_COMPONENT:{
                   if(definition.getDescriptor() instanceof AdrGroupDescriptor){
                       this.groupSettingsPanel.setPayload((AdrGroupDescriptor) definition.getDescriptor());
                       this.adrManagerFrame.setPage(this.groupSettingsPanel);
                   }
                   if(definition.getDescriptor() instanceof AdrIconDescriptor){
                       this.iconSettingsPanel.setFromGroup(definition.isFromGroup());
                       this.iconSettingsPanel.setPayload((AdrIconDescriptor) definition.getDescriptor());
                       this.adrManagerFrame.setPage(this.iconSettingsPanel);
                   }
                   break;
               }
               case REMOVE_COMPONENT: {
                   break;
               }
           }
        });
        MercuryStoreUI.adrSelectProfileSubject.subscribe(profile -> {
            this.selectedProfile.setSelected(false);
            this.selectedProfile = profile;
            //todo
            profile.setSelected(true);
        });
    }
}
