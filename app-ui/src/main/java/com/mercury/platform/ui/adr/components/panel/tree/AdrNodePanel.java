package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.AdrComponentsFactory;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;


public abstract class AdrNodePanel<D extends AdrComponentDescriptor> extends JPanel implements ViewInit {
    @Getter
    protected D descriptor;
    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    protected AdrComponentsFactory adrComponentsFactory = new AdrComponentsFactory(this.componentsFactory);
    protected AdrMouseOverListener mouseListener;
    protected AdrTreeNode<AdrComponentDescriptor> treeNode;

    public AdrNodePanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        this.treeNode = treeNode;
        this.descriptor = (D) treeNode.getData();
        this.mouseListener = new AdrMouseOverListener<>(this, descriptor, treeNode.getParent().getData() != null);
        this.addMouseListener(this.mouseListener);
        MercuryStoreUI.adrReloadSubject.subscribe(source -> {
            if (this.descriptor.equals(source)) {
                this.update();
            }
        });
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.SLIDE_BG);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.MSG_HEADER_BORDER));
        this.onViewInit();
    }

    protected abstract void update();
}
