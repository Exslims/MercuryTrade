package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProfileDescriptor;
import com.mercury.platform.ui.adr.components.panel.AdrPagePanel;
import com.mercury.platform.ui.adr.components.panel.ui.AdrTreeEntryCellRenderer;
import com.mercury.platform.ui.adr.routing.AdrPageDefinition;
import com.mercury.platform.ui.adr.routing.AdrPageState;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;

import javax.swing.*;
import javax.swing.plaf.IconUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class AdrManagerFrame extends AbstractTitledComponentFrame{
    private JPanel currentPage;
    private JPanel root;
    @Getter
    private AdrProfileDescriptor selectedProfile;
    public AdrManagerFrame(AdrProfileDescriptor selectedProfile) {
        super();
        this.selectedProfile = selectedProfile;
        UIManager.put("Tree.collapsedIcon",new IconUIResource(this.componentsFactory.getIcon("app/adr/node_collapse.png",22)));
        UIManager.put("Tree.expandedIcon",new IconUIResource(this.componentsFactory.getIcon("app/adr/node_expand.png",22)));
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.initRootPanel();
        this.add(this.root,BorderLayout.CENTER);

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
        DefaultMutableTreeNode model = new DefaultMutableTreeNode("test");

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

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(AppThemeColor.FRAME);
        leftPanel.setBorder(BorderFactory.createMatteBorder(0,0,0,1,AppThemeColor.BORDER));
        JTree tree = new JTree(model);
        tree.setCellRenderer(new AdrTreeEntryCellRenderer());
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setBorder(BorderFactory.createEmptyBorder(4,4,0,4));
        tree.setBackground(AppThemeColor.FRAME);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        tree.addTreeSelectionListener(e -> {
            if(tree.getSelectionCount() > 0) {
                MercuryStoreUI.adrSelectSubject.onNext(
                        new AdrPageDefinition<>(AdrPageState.OPERATIONS,
                                ((DefaultMutableTreeNode) e.getPath().getLastPathComponent()).getUserObject()));
            }
        });
        JButton addComponent = this.componentsFactory.getIconButton("app/adr/add_component.png", 20, AppThemeColor.FRAME, "todo");
        addComponent.setBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.BORDER));
        addComponent.addActionListener(action -> {
            tree.clearSelection();
            MercuryStoreUI.adrSelectSubject.onNext(new AdrPageDefinition<>(AdrPageState.MAIN,null));
        });
        leftPanel.add(addComponent,BorderLayout.PAGE_START);
        leftPanel.add(tree,BorderLayout.CENTER);
        this.root.add(leftPanel,BorderLayout.LINE_START);
    }

    @Override
    public void subscribe() {
    }

    @Override
    protected String getFrameTitle() {
        return "Aura Duration Tracking";
    }

}
