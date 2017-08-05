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

import javax.swing.*;
import java.awt.*;
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
    private AdrPagePanel<List<AdrProfileDescriptor>> profileSettingsPanel;

    private AdrLoadingPage loadingPage;

    private SwingWorker<Void,Void> worker;
    @Getter
    private AdrState state = AdrState.DEFAULT;
    public void load(){
        this.config = Configuration.get().adrConfiguration();

        this.groupSettingsPanel = new AdrGroupPagePanel();
        this.mainPanel = new AdrMainPagePanel(this.config);
        this.iconSettingsPanel = new AdrIconPagePanel();
        this.progressBarSettingsPanel = new AdrProgressBarPagePanel();
        this.profileSettingsPanel = new AdrProfilePagePanel();

        this.loadingPage = new AdrLoadingPage();

        this.selectedProfile = this.config.getEntities()
                .stream()
                .filter(AdrProfileDescriptor::isSelected)
                .findAny().orElse(null);
        this.adrManagerFrame = new AdrManagerFrame(this.selectedProfile);
        this.initComponents();
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
        this.frames.forEach(AbstractAdrFrame::enableSettings);
        this.adrManagerFrame.showComponent();
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
                case PROFILES_SETTINGS:{
                    this.profileSettingsPanel.setPayload(this.config.getEntities());
                    this.adrManagerFrame.setPage(this.profileSettingsPanel);
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
               case NEW_FROM_IMPORT:{
                   this.selectedProfile.getContents().addAll(definition.getDescriptors());
                   MercuryStoreCore.saveConfigSubject.onNext(true);
                   this.selectProfile(this.selectedProfile.getProfileName());
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
                targetFrame.onDestroy();
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
        MercuryStoreUI.adrSelectProfileSubject.subscribe(profileName -> {
            this.selectedProfile.setSelected(false);
            this.selectProfile(profileName);
        });
        MercuryStoreUI.adrNewProfileSubject.subscribe(profileName -> {
            AdrProfileDescriptor profileDescriptor = new AdrProfileDescriptor();
            profileDescriptor.setProfileName(profileName);
            profileDescriptor.setSelected(true);
            this.selectedProfile.setSelected(false);
            this.selectedProfile = profileDescriptor;

            this.config.getEntities().add(profileDescriptor);
            this.adrManagerFrame.addProfileToSelect(profileName);
            this.selectProfile(profileName);
            MercuryStoreCore.saveConfigSubject.onNext(true);
        });
        MercuryStoreUI.adrRemoveProfileSubject.subscribe(profile -> {
            this.config.getEntities().remove(profile);
            this.adrManagerFrame.removeProfileFromSelect(profile);
            MercuryStoreCore.saveConfigSubject.onNext(true);
        });
        MercuryStoreUI.adrRenameProfileSubject.subscribe(state -> {
            this.adrManagerFrame.onProfileRename(this.config.getEntities());
            MercuryStoreCore.saveConfigSubject.onNext(true);
        });
    }
    private void selectProfile(String profileName){
        this.adrManagerFrame.setPage(this.loadingPage);
        this.loadingPage.playLoop();
        AdrProfileDescriptor selectedProfile = this.config.getEntities()
                .stream()
                .filter(profile -> profile.getProfileName().equals(profileName))
                .findAny().orElse(null);
        selectedProfile.setSelected(true);
        this.selectedProfile = selectedProfile;

        this.worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                initComponents();
                frames.forEach(it -> {
                    it.init();
                    it.showComponent();
                    it.enableSettings();
                });
                return null;
            }

            @Override
            protected void done() {
                mainPanel.setFromGroup(false);
                mainPanel.setPayload(null);
                adrManagerFrame.setPage(mainPanel);
                loadingPage.abort();
                adrManagerFrame.onProfileLoaded();
                MercuryStoreUI.adrManagerPack.onNext(true);
            }
        };
        this.worker.execute();
        this.adrManagerFrame.setSelectedProfile(selectedProfile);
    }
    private void initComponents(){
        MercuryStoreUI.onDestroySubject.onNext(true);
        this.frames.forEach(Window::dispose);
        this.frames.clear();
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
    }
}
