package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryLoading;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdrTreePanel extends JPanel {
    @Getter
    @Setter
    private AdrTreeNodeRenderer renderer;
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    @Getter
    private List<AdrComponentDescriptor> descriptors;
    private JPanel container;
    private SwingWorker<Void, Void> worker;
    private MercuryLoading mercuryLoading;
    private JScrollPane verticalContainer;
    private AdrTreeNode<AdrComponentDescriptor> treeModel;

    public AdrTreePanel(List<AdrComponentDescriptor> descriptors, AdrTreeNodeRenderer renderer) {
        super(new BorderLayout());
        this.renderer = renderer;
        this.setBackground(AppThemeColor.FRAME_RGB);
        this.setPreferredSize(new Dimension(240, 10));
        this.descriptors = descriptors;
        this.container = new VerticalScrollContainer();
        this.container.setBackground(AppThemeColor.FRAME_RGB);
        this.container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        this.verticalContainer = this.componentsFactory.getVerticalContainer(container);

        this.mercuryLoading = new MercuryLoading();
        mercuryLoading.playLoop();
        this.add(mercuryLoading, BorderLayout.CENTER);
    }

    private void createTreeModel(AdrTreeNode<AdrComponentDescriptor> parent, List<AdrComponentDescriptor> source) {
        source.forEach(it -> {
            AdrTreeNode<AdrComponentDescriptor> treeNode = parent.addChild(it);
            JPanel viewOf = this.renderer.getViewOf(treeNode);
            treeNode.setPanel(viewOf);
            if (it.getType().equals(AdrComponentType.TRACKER_GROUP)) {
                this.createTreeModel(treeNode, ((AdrTrackerGroupDescriptor) it).getCells());
            }
        });
    }

    public void updateTree() {
        if (this.worker != null && !this.worker.isDone()) {
            return;
        }
        this.remove(this.verticalContainer);
        this.container.removeAll();
        this.mercuryLoading.playLoop();
        this.add(this.mercuryLoading, BorderLayout.CENTER);
        this.worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                treeModel = new AdrTreeNode<>(null, container);
                createTreeModel(treeModel, descriptors);
                return null;
            }

            @Override
            protected void done() {
                mercuryLoading.abort();
                remove(mercuryLoading);
                add(verticalContainer, BorderLayout.CENTER);
                MercuryStoreUI.adrManagerPack.onNext(true);
            }
        };
        this.worker.execute();
    }

    public void updateTree(List<AdrComponentDescriptor> descriptors) {
        this.descriptors = descriptors;
        this.updateTree();
    }

    public void addNode(AdrComponentDescriptor descriptor, AdrComponentDescriptor parentDescriptor) {
        this.addNodeHierarchy(this.treeModel, descriptor, parentDescriptor);
    }

    private void addNodeHierarchy(AdrTreeNode<AdrComponentDescriptor> parent,
                                  AdrComponentDescriptor descriptor,
                                  AdrComponentDescriptor parentDescriptor) {
        if (parentDescriptor == null) {
            AdrTreeNode<AdrComponentDescriptor> node = parent.addChild(descriptor);
            JPanel viewOf = this.renderer.getViewOf(node);
            node.setPanel(viewOf);
        } else {
            parent.forEach(it -> {
                if (parentDescriptor.equals(it.getData())) {
                    AdrTreeNode<AdrComponentDescriptor> node = it.addChild(descriptor);
                    JPanel viewOf = this.renderer.getViewOf(node);
                    node.setPanel(viewOf);
                }
                this.addNodeHierarchy(it, descriptor, parentDescriptor);
            });
        }
    }

    public void removeNode(AdrComponentDescriptor descriptor) {
        this.removeNode(this.treeModel, descriptor);
        MercuryStoreUI.adrManagerPack.onNext(true);
    }

    private void removeNode(AdrTreeNode<AdrComponentDescriptor> node, AdrComponentDescriptor descriptor) {
        node.removeChild(descriptor);
        node.forEach(it -> {
            it.removeChild(descriptor);
            this.removeNode(it, descriptor);
        });
    }

    public void duplicateNode(AdrComponentDescriptor descriptor) {
        this.treeModel.duplicateChild(descriptor);
    }
}
