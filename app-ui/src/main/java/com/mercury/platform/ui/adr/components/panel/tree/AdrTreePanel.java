package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdrTreePanel extends JPanel{
    @Getter @Setter
    private AdrTreeNodeRenderer renderer;
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    private List<AdrComponentDescriptor> descriptors;
    private JPanel container;
    public AdrTreePanel(List<AdrComponentDescriptor> descriptors, AdrTreeNodeRenderer renderer) {
        super(new BorderLayout());
        this.renderer = renderer;
        this.setPreferredSize(new Dimension(250,10));
        this.descriptors = descriptors;
        this.container = new VerticalScrollContainer();
        this.container.setBackground(AppThemeColor.FRAME);
        this.container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);
        this.add(verticalContainer,BorderLayout.CENTER);
        this.initTree(this.container,this.descriptors);
    }

    private void initTree(JPanel parent, List<AdrComponentDescriptor> child) {
        child.forEach(it -> {
            JPanel viewOf = this.renderer.getViewOf(it);
            if(it instanceof AdrGroupDescriptor){
                this.initTree(viewOf,((AdrGroupDescriptor) it).getCells());
                parent.add(this.componentsFactory.wrapToSlide(viewOf,2,4,2,4));
            }else {
                parent.add(this.componentsFactory.wrapToSlide(viewOf,2,4,2,4));
            }
        });
    }
}
