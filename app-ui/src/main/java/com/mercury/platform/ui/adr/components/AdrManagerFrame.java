package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.page.AdrPagePanel;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreePanel;
import com.mercury.platform.ui.adr.components.panel.tree.main.AdrMainTreeNodeRenderer;
import com.mercury.platform.ui.adr.dialog.AdrExportDialog;
import com.mercury.platform.ui.adr.dialog.AdrIconSelectDialog;
import com.mercury.platform.ui.adr.dialog.AdrNewProfileDialog;
import com.mercury.platform.ui.adr.routing.AdrPageDefinition;
import com.mercury.platform.ui.adr.routing.AdrPageState;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.InsetsUIResource;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdrManagerFrame extends AbstractTitledComponentFrame {
    private JPanel currentPage;
    private JPanel root;
    private AdrTreePanel tree;
    private JComboBox profileSelector;
    private AdrExportDialog exportDialog;
    private AdrIconSelectDialog iconSelectDialog;
    @Getter
    private AdrProfileDescriptor selectedProfile;

    public AdrManagerFrame(AdrProfileDescriptor selectedProfile) {
        super();
        this.processingHideEvent = false;
        this.setTitle("MercuryTrade ADR");
        this.setFocusable(true);
        this.setFocusableWindowState(true);
        this.setAlwaysOnTop(false);
        this.selectedProfile = selectedProfile;
        this.exportDialog = new AdrExportDialog(this, new ArrayList<>());
        this.iconSelectDialog = new AdrIconSelectDialog();
        this.iconSelectDialog.setLocationRelativeTo(null);
        FrameDescriptor frameDescriptor = this.framesConfig.get(this.getClass().getSimpleName());
        this.setPreferredSize(frameDescriptor.getFrameSize());
        UIManager.put("MenuItem.background", AppThemeColor.ADR_BG);
        UIManager.put("MenuItem.selectionBackground", AppThemeColor.ADR_POPUP_BG);
        UIManager.put("Menu.contentMargins", new InsetsUIResource(2, 0, 2, 0));
        UIManager.put("MenuItem.opaque", true);
        UIManager.put("ComboBox.selectionBackground", AppThemeColor.HEADER);
        UIManager.put("ComboBox.selectionForeground", AppThemeColor.ADR_POPUP_BG);
        UIManager.put("ComboBox.disabledForeground", AppThemeColor.ADR_FOOTER_BG);
    }

    @Override
    public void onViewInit() {
        this.initRootPanel();
        this.hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    MercuryStoreCore.showingDelaySubject.onNext(true);
                    FramesManager.INSTANCE.performAdr();
                }
            }
        });
        this.add(this.root, BorderLayout.CENTER);
        this.add(this.getBottomPanel(), BorderLayout.PAGE_END);
    }

    public void setPage(AdrPagePanel page) {
        if (currentPage != null) {
            this.root.remove(currentPage);
        }
        this.root.add(page, BorderLayout.CENTER);
        this.currentPage = page;
        this.pack();
        this.repaint();
    }

    private void initRootPanel() {
        this.root = componentsFactory.getTransparentPanel(new BorderLayout());
        this.root.setBackground(AppThemeColor.FRAME);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(AppThemeColor.FRAME);
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppThemeColor.BORDER));
        this.tree = new AdrTreePanel(
                this.selectedProfile.getContents(),
                new AdrMainTreeNodeRenderer());
        this.tree.updateTree();

        JButton addComponent = this.componentsFactory.getButton("New");
        addComponent.setBackground(AppThemeColor.FRAME);
        addComponent.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 20f));
        addComponent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                BorderFactory.createMatteBorder(1, 1, 1, 1, AppThemeColor.BORDER)));
        addComponent.addActionListener(action -> {
            MercuryStoreUI.adrSelectSubject.onNext(null);
            MercuryStoreUI.adrStateSubject.onNext(new AdrPageDefinition<>(AdrPageState.MAIN, null));
        });
        leftPanel.add(addComponent, BorderLayout.PAGE_START);
        leftPanel.add(this.tree, BorderLayout.CENTER);
        this.root.add(leftPanel, BorderLayout.LINE_START);
    }

    @Override
    public void subscribe() {
        MercuryStoreUI.adrReloadSubject.subscribe(state -> {
            this.tree.updateUI();
            MercuryStoreCore.saveConfigSubject.onNext(true);
        });
        MercuryStoreUI.adrManagerPack.subscribe(state -> {
            this.repaint();
            this.pack();
        });
        MercuryStoreUI.adrManagerRepaint.subscribe(state -> {
            this.repaint();
        });
        MercuryStoreUI.adrOpenIconSelectSubject.subscribe(callback -> {
            this.iconSelectDialog.setCallback(callback);
            this.iconSelectDialog.setVisible(true);
        });
    }

    public void setSelectedProfile(AdrProfileDescriptor profile) {
        this.selectedProfile = profile;
        this.tree.updateTree(profile.getContents());
    }

    public void onProfileLoaded() {
        this.profileSelector.setEnabled(true);
    }

    @Override
    protected String getFrameTitle() {
        return "Mercury: Overseer";
    }

    public void addNewNode(AdrComponentDescriptor descriptor, AdrComponentDescriptor parent) {
        this.tree.addNode(descriptor, parent);
        this.pack();
        this.repaint();
    }

    public void removeNode(AdrComponentDescriptor descriptor) {
        this.tree.removeNode(descriptor);
        this.pack();
        this.repaint();
    }

    public void duplicateNode(AdrComponentDescriptor descriptor) {
        this.tree.duplicateNode(descriptor);
        this.pack();
        this.repaint();
    }

    public void removeProfileFromSelect(AdrProfileDescriptor profile) {
        this.profileSelector.removeItem(profile.getProfileName());
    }

    public void addProfileToSelect(String profileName) {
        this.profileSelector.removeItem("Create new");
        this.profileSelector.addItem(profileName);
        this.profileSelector.addItem("Create new");
        this.profileSelector.setSelectedItem(profileName);
    }

    public void onProfileRename(List<AdrProfileDescriptor> entities) {
        this.profileSelector.removeAllItems();
        List<String> profilesNames = entities
                .stream()
                .map(AdrProfileDescriptor::getProfileName)
                .collect(Collectors.toList());
        profilesNames.add("Create new");
        profilesNames.forEach(it -> this.profileSelector.addItem(it));
        this.profileSelector.setSelectedItem(this.selectedProfile.getProfileName());
        this.repaint();
    }

    private JPanel getBottomPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppThemeColor.MSG_HEADER_BORDER));
        root.setBackground(AppThemeColor.ADR_FOOTER_BG);
        JPanel profilePanel = this.componentsFactory.getJPanel(new GridLayout(1, 3));
        profilePanel.setBackground(AppThemeColor.ADR_FOOTER_BG);
        List<String> profilesNames = Configuration.get()
                .adrConfiguration()
                .getEntities()
                .stream()
                .map(AdrProfileDescriptor::getProfileName)
                .collect(Collectors.toList());
        profilesNames.add("Create new");
        this.profileSelector = this.componentsFactory.getComboBox(profilesNames.toArray(new String[0]));
        this.profileSelector.setPreferredSize(new Dimension(120, 20));
        this.profileSelector.setBorder(BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER));
        this.profileSelector.setSelectedItem(this.selectedProfile.getProfileName());
        this.profileSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (!profileSelector.getSelectedItem().equals("Create new")) {
                    if (!profileSelector.getSelectedItem().equals(this.selectedProfile.getProfileName())) {
                        profileSelector.setEnabled(false);
                        MercuryStoreUI.adrSelectProfileSubject.onNext((String) profileSelector.getSelectedItem());
                    }
                } else {
                    profileSelector.setSelectedItem(this.selectedProfile.getProfileName());
                    new AdrNewProfileDialog(
                            MercuryStoreUI.adrNewProfileSubject::onNext,
                            profileSelector, profilesNames).setVisible(true);
                }
            }
        });
        JButton exportButton = this.componentsFactory.getIconButton("app/adr/export_node.png", 15, AppThemeColor.FRAME, TooltipConstants.ADR_EXPORT_BUTTON);
        exportButton.addActionListener(action -> {
            this.exportDialog.getPayload().addAll(this.selectedProfile.getContents());
            this.exportDialog.postConstruct();
            this.exportDialog.setVisible(true);
        });
        exportButton.setBackground(AppThemeColor.ADR_FOOTER_BG);
        profilePanel.add(exportButton);
        JPanel bottomPanel = this.componentsFactory.getJPanel(new BorderLayout());
        bottomPanel.setBackground(AppThemeColor.ADR_FOOTER_BG);
        profilePanel.add(this.componentsFactory.getTextLabel("Selected profile: "), BorderLayout.LINE_START);
        profilePanel.add(profileSelector);
//        JButton reloadButton = this.componentsFactory.getIconButton("app/adr/export_node.png", 15, AppThemeColor.FRAME, TooltipConstants.ADR_EXPORT_BUTTON);
//        reloadButton.addActionListener(action -> {
//            this.tree.updateTree();
//        });
//        profilePanel.add(reloadButton);
        JButton profileSettingsButton = this.componentsFactory.getIconButton("app/adr/profile_settings_icon.png", 18, AppThemeColor.FRAME, TooltipConstants.ADR_EXPORT_BUTTON);
        profileSettingsButton.addActionListener(action -> {
            MercuryStoreUI.adrStateSubject.onNext(new AdrPageDefinition<>(AdrPageState.PROFILES_SETTINGS, null));
        });
        profileSettingsButton.setBackground(AppThemeColor.ADR_FOOTER_BG);
        bottomPanel.add(exportButton, BorderLayout.LINE_START);
        bottomPanel.add(profilePanel, BorderLayout.CENTER);
        bottomPanel.add(profileSettingsButton, BorderLayout.LINE_END);

        root.add(bottomPanel, BorderLayout.LINE_START);
        root.add(profileSettingsButton, BorderLayout.LINE_END);
        return root;
    }
}
