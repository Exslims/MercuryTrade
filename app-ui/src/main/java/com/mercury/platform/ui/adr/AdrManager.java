package com.mercury.platform.ui.adr;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.AdrConfigurationService;
import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.*;
import com.mercury.platform.ui.adr.components.panel.page.*;
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
    private AdrPagePanel<AdrTrackerGroupDescriptor> groupSettingsPanel;
    private AdrPagePanel<AdrComponentDescriptor> mainPanel;
    private AdrPagePanel<AdrIconDescriptor> iconSettingsPanel;
    private AdrPagePanel<AdrProgressBarDescriptor> progressBarSettingsPanel;
    private AdrPagePanel<List<AdrProfileDescriptor>> profilesSettingsPanel;
    @Getter
    private AdrState state = AdrState.DEFAULT;
    public void load(){
        this.config = Configuration.get().adrConfiguration();

        this.groupSettingsPanel = new AdrGroupPagePanel();
        this.mainPanel = new AdrMainPagePanel(this.config);
        this.iconSettingsPanel = new AdrIconPagePanel();
        this.progressBarSettingsPanel = new AdrProgressBarPagePanel();
        this.profilesSettingsPanel = new AdrProfilePagePanel();

        this.selectedProfile = this.config.getEntities()
                .stream()
                .filter(AdrProfileDescriptor::isSelected)
                .findAny().orElse(null);
        this.adrManagerFrame = new AdrManagerFrame(this.selectedProfile);
        this.selectedProfile.getContents().forEach(component -> {
            switch (component.getType()){
                case TRACKER_GROUP: {
                    this.frames.add(new AdrTrackerGroupFrame((AdrTrackerGroupDescriptor) component));
                    break;
                }
                case PROGRESS_BAR:{
                    this.frames.add(new AdrSingleComponentFrame((AdrProgressBarDescriptor) component));
                    break;
                }
                case ICON:{
                    this.frames.add(new AdrSingleComponentFrame((AdrIconDescriptor) component));
                    break;
                }
            }
        });
        this.adrManagerFrame.init();
        this.frames.forEach(it -> {
            it.init();
            it.disableSettings();
        });

        this.subscribe();

        AdrFrameMagnet.INSTANCE.setDescriptors(this.selectedProfile.getContents());
        AdrFrameMagnet.INSTANCE.setDelta(10);

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
                    this.mainPanel.setFromGroup(definition.getPayload() != null);
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
                   if(definition.getDescriptor() instanceof AdrTrackerGroupDescriptor){
                       this.selectedProfile.getContents().add(definition.getDescriptor());
                       AdrTrackerGroupFrame adrTrackerGroupFrame =
                               new AdrTrackerGroupFrame((AdrTrackerGroupDescriptor) definition.getDescriptor());
                       adrTrackerGroupFrame.init();
                       adrTrackerGroupFrame.showComponent();
                       adrTrackerGroupFrame.enableSettings();
                       this.frames.add(adrTrackerGroupFrame);
                       this.groupSettingsPanel.setPayload((AdrTrackerGroupDescriptor) definition.getDescriptor());
                       this.adrManagerFrame.setPage(this.groupSettingsPanel);
                   }
                   if(definition.getDescriptor() instanceof AdrDurationComponentDescriptor){
                       if(definition.getParent() == null){
                           this.selectedProfile.getContents().add(definition.getDescriptor());
                           AdrSingleComponentFrame componentFrame =
                                   new AdrSingleComponentFrame((AdrDurationComponentDescriptor) definition.getDescriptor());
                           componentFrame.init();
                           componentFrame.showComponent();
                           componentFrame.enableSettings();
                           this.frames.add(componentFrame);
                       }else {
                           ((AdrTrackerGroupDescriptor)definition.getParent()).getCells().add(definition.getDescriptor());
                       }
                       if(definition.getDescriptor() instanceof AdrIconDescriptor){
                           this.iconSettingsPanel.setFromGroup(definition.getParent() != null);
                           this.iconSettingsPanel.setPayload((AdrIconDescriptor) definition.getDescriptor());
                           this.adrManagerFrame.setPage(this.iconSettingsPanel);
                       }else {
                           this.progressBarSettingsPanel.setFromGroup(definition.getParent() != null);
                           this.progressBarSettingsPanel.setPayload((AdrProgressBarDescriptor) definition.getDescriptor());
                           this.adrManagerFrame.setPage(this.progressBarSettingsPanel);
                       }
                   }
                   this.adrManagerFrame.addNewNode(definition.getDescriptor(),definition.getParent());
                   MercuryStoreUI.adrSelectSubject.onNext(definition.getDescriptor());
                   MercuryStoreCore.saveConfigSubject.onNext(true);
                   MercuryStoreUI.adrPostOperationsComponentSubject.onNext(definition.getDescriptor());
                   break;
               }
               case EDIT_COMPONENT:{
                   if(definition.getDescriptor() instanceof AdrTrackerGroupDescriptor){
                       this.groupSettingsPanel.setPayload((AdrTrackerGroupDescriptor) definition.getDescriptor());
                       this.adrManagerFrame.setPage(this.groupSettingsPanel);
                   }
                   if(definition.getDescriptor() instanceof AdrIconDescriptor){
                       this.iconSettingsPanel.setFromGroup(definition.isFromGroup());
                       this.iconSettingsPanel.setPayload((AdrIconDescriptor) definition.getDescriptor());
                       this.adrManagerFrame.setPage(this.iconSettingsPanel);
                   }
                   if(definition.getDescriptor() instanceof AdrProgressBarDescriptor){
                       this.progressBarSettingsPanel.setFromGroup(definition.isFromGroup());
                       this.progressBarSettingsPanel.setPayload((AdrProgressBarDescriptor) definition.getDescriptor());
                       this.adrManagerFrame.setPage(this.progressBarSettingsPanel);
                   }
                   break;
               }
               case DUPLICATE_COMPONENT:{
                   this.adrManagerFrame.duplicateNode(definition.getDescriptor());
                   MercuryStoreUI.adrSelectSubject.onNext(definition.getDescriptor());
                   MercuryStoreCore.saveConfigSubject.onNext(true);
                   MercuryStoreUI.adrPostOperationsComponentSubject.onNext(definition.getDescriptor());
                   break;
               }
           }
        });
        MercuryStoreUI.adrRemoveComponentSubject.subscribe(descriptor -> {
            AbstractAdrFrame targetFrame =
                    this.frames.stream()
                            .filter(it -> it.getDescriptor().equals(descriptor))
                            .findAny().orElse(null);
            if(targetFrame != null) {
                this.frames.remove(targetFrame);
                this.selectedProfile.getContents().remove(descriptor);
                targetFrame.dispose();
            }
            this.mainPanel.setFromGroup(false);
            this.mainPanel.setPayload(null);
            this.adrManagerFrame.removeNode(descriptor);
            this.adrManagerFrame.setPage(this.mainPanel);
            MercuryStoreCore.saveConfigSubject.onNext(true);
            MercuryStoreUI.adrPostOperationsComponentSubject.onNext(descriptor);
        });
        MercuryStoreUI.adrSelectProfileSubject.subscribe(profile -> {
            this.selectedProfile.setSelected(false);
            this.selectedProfile = profile;
            //todo
            profile.setSelected(true);
        });
    }
}
