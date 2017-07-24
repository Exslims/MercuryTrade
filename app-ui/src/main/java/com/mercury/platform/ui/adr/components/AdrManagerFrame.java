package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.ui.adr.components.panel.page.AdrPagePanel;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreePanel;
import com.mercury.platform.ui.adr.components.panel.ui.AdrTreeEntryCellRenderer;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.adr.routing.AdrPageDefinition;
import com.mercury.platform.ui.adr.routing.AdrPageState;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AdrManagerFrame extends AbstractTitledComponentFrame{
    private JPanel currentPage;
    private JPanel root;
//    private JTree componentsTree;
    @Getter
    private AdrProfileDescriptor selectedProfile;
    public AdrManagerFrame(AdrProfileDescriptor selectedProfile) {
        super();
        this.setFocusable(true);
        this.setFocusableWindowState(true);
        this.setAlwaysOnTop(false);
        this.selectedProfile = selectedProfile;
        UIManager.put("Tree.collapsedIcon",new IconUIResource(this.componentsFactory.getIcon("app/adr/node_collapse.png",22)));
        UIManager.put("Tree.expandedIcon",new IconUIResource(this.componentsFactory.getIcon("app/adr/node_expand.png",22)));
        UIManager.getLookAndFeelDefaults().put("Menu.arrowIcon", null);
        UIManager.put("MenuItem.background", AppThemeColor.FRAME);
        UIManager.put("MenuItem.opaque", true);
        UIManager.put("ComboBox.selectionBackground", AppThemeColor.HEADER);
        UIManager.put("ComboBox.selectionForeground", AppThemeColor.TEXT_DEFAULT);
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
//        this.componentsTree = new JTree(this.getTreeModel());
//        this.componentsTree.setCellRenderer(new AdrTreeEntryCellRenderer());
//        this.componentsTree.setRootVisible(false);
//        this.componentsTree.setShowsRootHandles(true);
//        this.componentsTree.setBorder(BorderFactory.createEmptyBorder(4,4,0,4));
//        this.componentsTree.setBackground(AppThemeColor.FRAME);
//        this.componentsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//
//        this.componentsTree.addTreeSelectionListener(e -> {
//            if(this.componentsTree.getSelectionCount() > 0) {
//                MercuryStoreUI.adrComponentStateSubject.onNext(
//                        new AdrComponentDefinition(
//                                (AdrComponentDescriptor)((DefaultMutableTreeNode) e.getPath().getLastPathComponent()).getUserObject(),
//                                AdrComponentOperations.EDIT_COMPONENT,
//                                e.getPath().getPathCount() == 3));
//            }
//        });
//        this.componentsTree.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                repaint();
//                if(SwingUtilities.isRightMouseButton(e)){
//                    componentsTree.setSelectionRow(componentsTree.getClosestRowForLocation(e.getX(),e.getY()));
//                    TreePath pathForLocation = componentsTree.getPathForLocation(e.getX(), e.getY());
//                    if (pathForLocation != null) {
//                        getContextMenu((AdrComponentDescriptor)
//                                ((DefaultMutableTreeNode)pathForLocation.getLastPathComponent()).getUserObject())
//                                .show(e.getComponent(),e.getX(),e.getY());
//                    }
//                }
//            }
//        });

//        JScrollPane scrollPane = new JScrollPane(this.componentsTree);
//        scrollPane.setBorder(null);
//        scrollPane.setBackground(AppThemeColor.FRAME);
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        JScrollBar vBar = scrollPane.getVerticalScrollBar();
//        vBar.setBackground(AppThemeColor.SLIDE_BG);
//        vBar.setUI(new MercuryScrollBarUI());
//        vBar.setPreferredSize(new Dimension(14, Integer.MAX_VALUE));
//        vBar.setUnitIncrement(3);
//        vBar.setBorder(BorderFactory.createEmptyBorder(1,1,1,2));
//        vBar.addAdjustmentListener(e -> repaint());

        JButton addComponent = this.componentsFactory.getButton("New");
        addComponent.setBackground(AppThemeColor.FRAME);
        addComponent.setFont(this.componentsFactory.getFont(FontStyle.BOLD,20f));
        addComponent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2,2,2,2),
                BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.BORDER)));
        addComponent.addActionListener(action -> {
//            this.componentsTree.clearSelection();
            MercuryStoreUI.adrStateSubject.onNext(new AdrPageDefinition<>(AdrPageState.MAIN,null));
        });
        leftPanel.add(addComponent,BorderLayout.PAGE_START);
        leftPanel.add(new AdrTreePanel(this.selectedProfile.getContents()),BorderLayout.CENTER);
        this.root.add(leftPanel,BorderLayout.LINE_START);
    }

    @Override
    public void subscribe() {
//        MercuryStoreUI.adrReloadSubject.subscribe(state -> {
//            this.componentsTree.updateUI();
//        });
        MercuryStoreUI.adrUpdateTree.subscribe(state -> {
            this.repaint();
            this.pack();
        });
    }

    @Override
    protected String getFrameTitle() {
        return "Aura Duration Tracking";
    }

    public void reloadTree() {
//        DefaultTreeModel model = (DefaultTreeModel) this.componentsTree.getModel();
//        model.setRoot(getTreeModel());
        this.pack();
        this.repaint();
    }
    private DefaultMutableTreeNode getTreeModel() {
        DefaultMutableTreeNode model = new DefaultMutableTreeNode("root");
        this.selectedProfile.getContents().forEach(descriptor -> {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(descriptor);
            switch (descriptor.getType()){
                case GROUP: {
                    ((AdrGroupDescriptor)descriptor).getCells().forEach(cell -> {
                        DefaultMutableTreeNode cellNode = new DefaultMutableTreeNode(cell);
                        node.add(cellNode);
                    });
                    break;
                }
            }
            model.add(node);
        });
        return model;
    }
    private JPopupMenu getContextMenu(AdrComponentDescriptor selectedDescriptor) {
        JPopupMenu contextMenu = this.componentsFactory.getContextPanel();
        switch (selectedDescriptor.getType()){
            case GROUP: {
                JMenuItem addComponent = this.componentsFactory.getMenuItem("Add");
                JMenuItem iconComponent = this.componentsFactory.getMenuItem("Icon");
                JMenuItem pbComponent = this.componentsFactory.getMenuItem("Progress bar");
                addComponent.add(iconComponent);
                addComponent.add(pbComponent);
                contextMenu.add(addComponent);
                break;
            }
        }
        JMenuItem duplicateComponent = this.componentsFactory.getMenuItem("Duplicate");
        JMenuItem removeComponent = this.componentsFactory.getMenuItem("Remove");
        contextMenu.add(duplicateComponent);
        contextMenu.add(removeComponent);
        return contextMenu;
    }
    private JPanel getBottomPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBorder(BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.MSG_HEADER_BORDER));
        root.setBackground(AppThemeColor.HEADER);
        JPanel profilePanel = this.componentsFactory.getJPanel(new GridLayout(1, 2));
        profilePanel.setBackground(AppThemeColor.HEADER);
        profilePanel.add(this.componentsFactory.getTextLabel("Selected profile: "),BorderLayout.LINE_START);
        List<String> profilesNames = Configuration.get().adrConfiguration().getEntities().stream().map(AdrProfileDescriptor::getProfileName).collect(Collectors.toList());
        JComboBox profileSelector = this.componentsFactory.getComboBox(profilesNames.toArray(new String[0]));
        profileSelector.setBorder(BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER));
        profilePanel.add(profileSelector);
        JButton openProfileSettings = this.componentsFactory.getIconButton("app/adr/profile_settings_icon.png", 20, AppThemeColor.HEADER, TooltipConstants.ADR_PROFILE_SETTINGS);
        openProfileSettings.addActionListener(action -> {
//            this.componentsTree.clearSelection();
            MercuryStoreUI.adrStateSubject.onNext(new AdrPageDefinition<>(AdrPageState.PROFILE,
                    Configuration.get().adrConfiguration().getEntities()));
        });
        root.add(profilePanel,BorderLayout.LINE_START);
        root.add(openProfileSettings,BorderLayout.LINE_END);
        return root;
    }
}
