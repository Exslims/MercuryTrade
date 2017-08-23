package com.mercury.platform.ui.adr;


import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.AdrConfigurationService;
import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.AbstractAdrFrame;
import com.mercury.platform.ui.adr.components.AdrCaptureOutComponentFrame;
import com.mercury.platform.ui.adr.components.AdrManagerFrame;
import com.mercury.platform.ui.adr.components.factory.FrameProviderFactory;
import com.mercury.platform.ui.adr.components.panel.AdrCaptureOutPanel;
import com.mercury.platform.ui.adr.components.panel.page.*;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdrManager implements AsSubscriber {
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
    private AdrPagePanel<AdrCaptureDescriptor> captureSettingsPanel;

    private AdrLoadingPage loadingPage;

    private FrameProviderFactory frameProviderFactory;
    @Getter
    private AdrState state = AdrState.DEFAULT;

    public void load() {
        this.config = Configuration.get().adrConfiguration();

        this.frameProviderFactory = new FrameProviderFactory();

        this.groupSettingsPanel = new AdrGroupPagePanel();
        this.mainPanel = new AdrMainPagePanel(this.config);
        this.iconSettingsPanel = new AdrIconPagePanel();
        this.progressBarSettingsPanel = new AdrProgressBarPagePanel();
        this.profileSettingsPanel = new AdrProfilePagePanel();
        this.captureSettingsPanel = new AdrCapturePagePanel();

        this.loadingPage = new AdrLoadingPage();

        this.selectedProfile = this.config.getEntities()
                .stream()
                .filter(AdrProfileDescriptor::isSelected)
                .findAny().orElse(null);
        this.adrManagerFrame = new AdrManagerFrame(this.selectedProfile);
        this.initComponents(false);
        this.adrManagerFrame.init();

        this.subscribe();

        AdrFrameMagnet.INSTANCE.setDescriptors(this.selectedProfile.getContents());
        AdrFrameMagnet.INSTANCE.setDelta(10);

        this.mainPanel.setPayload(null);
        this.adrManagerFrame.setPage(this.mainPanel);
    }

    public void enableSettings() {
        this.state = AdrState.SETTINGS;
        this.frames.forEach(AbstractAdrFrame::enableSettings);
        this.adrManagerFrame.showComponent();
    }

    public void disableSettings() {
        this.state = AdrState.DEFAULT;
        this.adrManagerFrame.hideComponent();
        this.frames.forEach(AbstractAdrFrame::disableSettings);
    }

    @Override
    @SuppressWarnings("all")
    public void subscribe() {
        MercuryStoreUI.adrStateSubject.subscribe(definition -> {
            switch (definition.getState()) {
                case MAIN: {
                    this.mainPanel.setFromGroup(definition.getPayload() != null);
                    this.mainPanel.setPayload((AdrComponentDescriptor) definition.getPayload());
                    this.adrManagerFrame.setPage(this.mainPanel);
                    break;
                }
                case PROFILES_SETTINGS: {
                    this.profileSettingsPanel.setPayload(this.config.getEntities());
                    this.adrManagerFrame.setPage(this.profileSettingsPanel);
                    break;
                }
            }
        });
        MercuryStoreUI.adrComponentStateSubject.subscribe(definition -> {
            switch (definition.getOperations()) {
                case NEW_COMPONENT: {
                    if (definition.getDescriptor() instanceof AdrTrackerGroupDescriptor) {
                        this.selectedProfile.getContents().add(definition.getDescriptor());
                        AbstractAdrFrame frame = this.frameProviderFactory
                                .getProviderFor(definition.getDescriptor())
                                .getFrame(true);
                        this.frames.add(frame);
                        this.groupSettingsPanel.setPayload((AdrTrackerGroupDescriptor) definition.getDescriptor());
                        this.adrManagerFrame.setPage(this.groupSettingsPanel);
                    }
                    if (definition.getDescriptor() instanceof AdrDurationComponentDescriptor) {
                        if (definition.getParent() == null) {
                            this.selectedProfile.getContents().add(definition.getDescriptor());
                            AbstractAdrFrame componentFrame = this.frameProviderFactory
                                    .getProviderFor(definition.getDescriptor())
                                    .getFrame(true);
                            this.frames.add(componentFrame);
                        } else {
                            ((AdrTrackerGroupDescriptor) definition.getParent()).getCells().add(definition.getDescriptor());
                        }
                        if (definition.getDescriptor() instanceof AdrIconDescriptor) {
                            this.iconSettingsPanel.setFromGroup(definition.getParent() != null);
                            this.iconSettingsPanel.setPayload((AdrIconDescriptor) definition.getDescriptor());
                            this.adrManagerFrame.setPage(this.iconSettingsPanel);
                        } else {
                            this.progressBarSettingsPanel.setFromGroup(definition.getParent() != null);
                            this.progressBarSettingsPanel.setPayload((AdrProgressBarDescriptor) definition.getDescriptor());
                            this.adrManagerFrame.setPage(this.progressBarSettingsPanel);
                        }
                    }
                    if (definition.getDescriptor() instanceof AdrCaptureDescriptor) {
                        AdrCaptureDescriptor descriptor = (AdrCaptureDescriptor) definition.getDescriptor();
                        this.frames.add(this.frameProviderFactory.getProviderFor(descriptor).getFrame(true));

                        AdrCaptureOutComponentFrame outFrame = new AdrCaptureOutComponentFrame((AdrCaptureDescriptor) descriptor);
                        outFrame.setPanel(new AdrCaptureOutPanel((AdrCaptureDescriptor) descriptor, new ComponentsFactory()));
                        outFrame.init();
                        outFrame.showComponent();
                        outFrame.enableSettings();
                        this.frames.add(outFrame);

                        this.captureSettingsPanel.setPayload((AdrCaptureDescriptor) definition.getDescriptor());
                        this.adrManagerFrame.setPage(this.captureSettingsPanel);

                        this.selectedProfile.getContents().add(definition.getDescriptor());
                    }
                    this.adrManagerFrame.addNewNode(definition.getDescriptor(), definition.getParent());
                    MercuryStoreUI.adrSelectSubject.onNext(definition.getDescriptor());
                    MercuryStoreCore.saveConfigSubject.onNext(true);
                    MercuryStoreUI.adrPostOperationsComponentSubject.onNext(definition.getDescriptor());
                    break;
                }
                case EDIT_COMPONENT: {
                    if (definition.getDescriptor() instanceof AdrTrackerGroupDescriptor) {
                        this.groupSettingsPanel.setPayload((AdrTrackerGroupDescriptor) definition.getDescriptor());
                        this.adrManagerFrame.setPage(this.groupSettingsPanel);
                    }
                    if (definition.getDescriptor() instanceof AdrIconDescriptor) {
                        this.iconSettingsPanel.setFromGroup(definition.isFromGroup());
                        this.iconSettingsPanel.setPayload((AdrIconDescriptor) definition.getDescriptor());
                        this.adrManagerFrame.setPage(this.iconSettingsPanel);
                    }
                    if (definition.getDescriptor() instanceof AdrProgressBarDescriptor) {
                        this.progressBarSettingsPanel.setFromGroup(definition.isFromGroup());
                        this.progressBarSettingsPanel.setPayload((AdrProgressBarDescriptor) definition.getDescriptor());
                        this.adrManagerFrame.setPage(this.progressBarSettingsPanel);
                    }
                    if (definition.getDescriptor() instanceof AdrCaptureDescriptor) {
                        this.captureSettingsPanel.setPayload((AdrCaptureDescriptor) definition.getDescriptor());
                        this.adrManagerFrame.setPage(this.captureSettingsPanel);
                    }
                    break;
                }
                case DUPLICATE_COMPONENT: {
                    this.adrManagerFrame.duplicateNode(definition.getDescriptor());
                    MercuryStoreUI.adrSelectSubject.onNext(definition.getDescriptor());
                    MercuryStoreCore.saveConfigSubject.onNext(true);
                    MercuryStoreUI.adrPostOperationsComponentSubject.onNext(definition.getDescriptor());
                    break;
                }
                case NEW_FROM_IMPORT: {
                    this.selectedProfile.getContents().addAll(definition.getDescriptors());
                    MercuryStoreCore.saveConfigSubject.onNext(true);
                    this.selectProfile(this.selectedProfile.getProfileName());
                    break;
                }
            }
        });
        MercuryStoreUI.adrRemoveComponentSubject.subscribe(descriptor -> {
            List<AbstractAdrFrame> targetFrames =
                    this.frames.stream()
                            .filter(it -> it.getDescriptor().equals(descriptor)).collect(Collectors.toList());
            targetFrames.forEach(targetFrame -> {
                targetFrame.onDestroy();
                targetFrame.disableSettings();
                targetFrame.setVisible(false);
                this.frames.remove(targetFrame);
                this.selectedProfile.getContents().remove(descriptor);
                targetFrame.dispose();
            });

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

    private void selectProfile(String profileName) {
        this.adrManagerFrame.setPage(this.loadingPage);
        this.loadingPage.playLoop();
        AdrProfileDescriptor selectedProfile = this.config.getEntities()
                .stream()
                .filter(profile -> profile.getProfileName().equals(profileName))
                .findAny().orElse(null);
        selectedProfile.setSelected(true);
        this.selectedProfile = selectedProfile;

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                initComponents(true);
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
        worker.execute();
        this.adrManagerFrame.setSelectedProfile(selectedProfile);
        MercuryStoreCore.saveConfigSubject.onNext(true);
    }

    private void initComponents(boolean showSettings) {
        MercuryStoreUI.onDestroySubject.onNext(true);
        this.frames.forEach(it -> {
            it.onDestroy();
            it.disableSettings();
            it.setVisible(false);
            it.dispose();
        });
        this.frames.clear();
        this.selectedProfile.getContents().forEach(component -> {
            if (component instanceof AdrCaptureDescriptor) {
                this.frames.add(this.frameProviderFactory.getProviderFor(component).getFrame(showSettings));
                AdrCaptureOutComponentFrame outFrame = new AdrCaptureOutComponentFrame((AdrCaptureDescriptor) component);
                outFrame.setPanel(new AdrCaptureOutPanel((AdrCaptureDescriptor) component, new ComponentsFactory()));
                outFrame.init();
                if (showSettings) {
                    outFrame.showComponent();
                    outFrame.enableSettings();
                } else {
                    outFrame.disableSettings();
                }
                this.frames.add(outFrame);
            } else {
                this.frames.add(this.frameProviderFactory.getProviderFor(component).getFrame(showSettings));
            }
        });
    }
}
