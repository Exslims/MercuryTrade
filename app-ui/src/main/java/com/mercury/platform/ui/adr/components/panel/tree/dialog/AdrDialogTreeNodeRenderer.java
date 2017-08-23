package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrTreeNodeRenderer;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.components.ComponentsFactory;

import javax.swing.*;


public class AdrDialogTreeNodeRenderer implements AdrTreeNodeRenderer {
    private ComponentsFactory componentsFactory = new ComponentsFactory();

    @Override
    public JPanel getViewOf(AdrTreeNode<AdrComponentDescriptor> node) {
        switch (node.getData().getType()) {
            case ICON: {
                return this.componentsFactory.wrapToAdrSlide(new AdrDialogIconNodePanel(node), 2, 4, 2, 4);
            }
            case PROGRESS_BAR: {
                return this.componentsFactory.wrapToAdrSlide(new AdrDialogPBNodePanel(node), 2, 4, 2, 4);
            }
            case TRACKER_GROUP: {
                return this.componentsFactory.wrapToAdrSlide(new AdrDialogGroupNodePanel(node), 2, 4, 2, 4);
            }
            case CAPTURE: {
                return this.componentsFactory.wrapToAdrSlide(new AdrDialogCaptureNodePanel(node), 2, 4, 2, 4);
            }
        }
        return new JPanel();
    }
}
