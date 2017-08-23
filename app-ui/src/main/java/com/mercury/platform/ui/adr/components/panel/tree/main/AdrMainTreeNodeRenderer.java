package com.mercury.platform.ui.adr.components.panel.tree.main;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreeNodeRenderer;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.components.ComponentsFactory;

import javax.swing.*;


public class AdrMainTreeNodeRenderer implements AdrTreeNodeRenderer {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public JPanel getViewOf(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        switch (treeNode.getData().getType()) {
            case ICON: {
                return this.componentsFactory.wrapToAdrSlide(new AdrIconNodePanel(treeNode), 2, 4, 2, 4);
            }
            case PROGRESS_BAR: {
                return this.componentsFactory.wrapToAdrSlide(new AdrProgressBarNodePanel(treeNode), 2, 4, 2, 4);
            }
            case TRACKER_GROUP: {
                return this.componentsFactory.wrapToAdrSlide(new AdrGroupNodePanel(treeNode), 2, 4, 2, 4);
            }
            case CAPTURE: {
                return this.componentsFactory.wrapToSlide(new AdrCaptureNodePanel(treeNode), 2, 4, 2, 4);
            }
        }
        return new JPanel();
    }
}
