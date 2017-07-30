package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.ui.adr.components.panel.page.AdrPagePanel;
import com.mercury.platform.ui.adr.components.panel.tree.main.AdrMainTreeNodeRenderer;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreePanel;
import com.mercury.platform.ui.adr.dialog.AdrNewProfileDialog;
import com.mercury.platform.ui.adr.dialog.ExportHelper;
import com.mercury.platform.ui.adr.routing.AdrPageDefinition;
import com.mercury.platform.ui.adr.routing.AdrPageState;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.stream.Collectors;

public class AdrManagerFrame extends AbstractTitledComponentFrame{
    private JPanel currentPage;
    private JPanel root;
    private AdrTreePanel tree;
    private JComboBox profileSelector;
    @Getter
    private AdrProfileDescriptor selectedProfile;
    public AdrManagerFrame(AdrProfileDescriptor selectedProfile) {
        super();
        this.setFocusable(true);
        this.setFocusableWindowState(true);
        this.setAlwaysOnTop(false);
        this.selectedProfile = selectedProfile;
        UIManager.put("MenuItem.background", AppThemeColor.ADR_BG);
        UIManager.put("MenuItem.selectionBackground", AppThemeColor.ADR_POPUP_BG);
        UIManager.put("Menu.contentMargins", new InsetsUIResource(2,0,2,0));
        UIManager.put("MenuItem.opaque", true);
        UIManager.put("ComboBox.selectionBackground", AppThemeColor.HEADER);
        UIManager.put("ComboBox.selectionForeground", AppThemeColor.ADR_POPUP_BG);
        UIManager.put("ComboBox.disabledForeground",  AppThemeColor.ADR_FOOTER_BG);
        this.subscribe();
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.initRootPanel();
        this.add(this.root,BorderLayout.CENTER);
        this.add(this.getBottomPanel(),BorderLayout.PAGE_END);

    }
    public void setPage(AdrPagePanel page){
        if(currentPage != null){
            this.root.remove(currentPage);
        }
        this.root.add(page,BorderLayout.CENTER);
        this.currentPage = page;
        this.pack();
        this.repaint();
    }
    private void initRootPanel(){
        this.root = componentsFactory.getTransparentPanel(new BorderLayout());
        this.root.setBackground(AppThemeColor.FRAME);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(AppThemeColor.FRAME);
        leftPanel.setBorder(BorderFactory.createMatteBorder(0,0,0,1,AppThemeColor.BORDER));
        this.tree = new AdrTreePanel(
                this.selectedProfile.getContents(),
                new AdrMainTreeNodeRenderer());
        this.tree.updateTree();

        JButton addComponent = this.componentsFactory.getButton("New");
        addComponent.setBackground(AppThemeColor.FRAME);
        addComponent.setFont(this.componentsFactory.getFont(FontStyle.BOLD,20f));
        addComponent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2,2,2,2),
                BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.BORDER)));
        addComponent.addActionListener(action -> {
            MercuryStoreUI.adrSelectSubject.onNext(null);
            MercuryStoreUI.adrStateSubject.onNext(new AdrPageDefinition<>(AdrPageState.MAIN,null));
        });
        leftPanel.add(addComponent,BorderLayout.PAGE_START);
        leftPanel.add(this.tree,BorderLayout.CENTER);
        this.root.add(leftPanel,BorderLayout.LINE_START);
    }

    @Override
    public void subscribe() {
        MercuryStoreUI.adrReloadSubject.subscribe(state -> {
            this.tree.updateUI();
        });
        MercuryStoreUI.adrManagerPack.subscribe(state -> {
            this.repaint();
            this.pack();
        });
    }

    public void setSelectedProfile(AdrProfileDescriptor profile){
        this.selectedProfile = profile;
        this.tree.updateTree(profile.getContents());
    }
    public void onProfileLoaded(){
        this.profileSelector.setEnabled(true);
    }

    @Override
    protected String getFrameTitle() {
        return "Aura Duration Tracker";
    }

    public void addNewNode(AdrComponentDescriptor descriptor, AdrComponentDescriptor parent) {
        this.tree.addNode(descriptor,parent);
        this.pack();
        this.repaint();
    }
    public void removeNode(AdrComponentDescriptor descriptor) {
        this.tree.removeNode(descriptor);
        this.pack();
        this.repaint();
    }
    public void duplicateNode(AdrComponentDescriptor descriptor){
        this.tree.duplicateNode(descriptor);
        this.pack();
        this.repaint();
    }
    private JPanel getBottomPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBorder(BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.MSG_HEADER_BORDER));
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
        profileSelector = this.componentsFactory.getComboBox(profilesNames.toArray(new String[0]));
        profileSelector.setBorder(BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER));
        profileSelector.setSelectedItem(this.selectedProfile.getProfileName());
        profileSelector.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                if(!profileSelector.getSelectedItem().equals("Create new")) {
                    if(!profileSelector.getSelectedItem().equals(this.selectedProfile.getProfileName())) {
                        profileSelector.setEnabled(false);
                        MercuryStoreUI.adrSelectProfileSubject.onNext((String) profileSelector.getSelectedItem());
                    }
                }else {
                    profileSelector.setSelectedItem(this.selectedProfile.getProfileName());
                    new AdrNewProfileDialog(profileName -> {
                        System.out.println(profileName);
                    },profileSelector,profilesNames).setVisible(true);
                }
            }
        });
        JButton exportButton = this.componentsFactory.getIconButton("app/adr/export_node.png", 15, AppThemeColor.FRAME, TooltipConstants.ADR_EXPORT_BUTTON);
        exportButton.addActionListener(action -> {
            ExportHelper.exportProfile(this.selectedProfile);
        });
        exportButton.setBackground(AppThemeColor.ADR_FOOTER_BG);
        profilePanel.add(exportButton);
        JPanel bottomPanel = this.componentsFactory.getJPanel(new BorderLayout());
        bottomPanel.setBackground(AppThemeColor.ADR_FOOTER_BG);
        profilePanel.add(this.componentsFactory.getTextLabel("Selected profile: "),BorderLayout.LINE_START);
        profilePanel.add(profileSelector);
//        JButton reloadButton = this.componentsFactory.getIconButton("app/adr/export_node.png", 15, AppThemeColor.FRAME, TooltipConstants.ADR_EXPORT_BUTTON);
//        reloadButton.addActionListener(action -> {
//            this.tree.updateTree();
//        });
//        profilePanel.add(reloadButton);
        bottomPanel.add(exportButton,BorderLayout.LINE_START);
        bottomPanel.add(profilePanel,BorderLayout.CENTER);

        root.add(bottomPanel,BorderLayout.LINE_START);
        return root;
    }
}
