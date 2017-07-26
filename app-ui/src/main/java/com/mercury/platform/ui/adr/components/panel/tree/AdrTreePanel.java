package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.experimental.PerformanceHelper;
import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AdrTreePanel extends JPanel{
    @Getter @Setter
    private AdrTreeNodeRenderer renderer;
    private ComponentsFactory componentsFactory = new ComponentsFactory();
    private List<AdrComponentDescriptor> descriptors;
    private JPanel container;
    private PerformanceHelper performanceHelper = new PerformanceHelper();
    public AdrTreePanel(List<AdrComponentDescriptor> descriptors, AdrTreeNodeRenderer renderer) {
        super(new BorderLayout());
        performanceHelper.reset();
        this.renderer = renderer;
        this.setPreferredSize(new Dimension(280,10));
        this.descriptors = descriptors;
        this.container = new VerticalScrollContainer();
        this.container.setBackground(AppThemeColor.FRAME);
        this.container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);
        this.add(verticalContainer,BorderLayout.CENTER);
    }
    public void updateTree(){
        this.updateTree(this.descriptors);
    }
    public void updateTree(List<AdrComponentDescriptor> descriptors){
        this.descriptors = descriptors;
        this.container.removeAll();
        this.initTree(this.container,this.descriptors,false);
    }
    public void addNode(AdrComponentDescriptor descriptor,boolean inner){
        this.addNodeHierarchy(this.container, Arrays.asList(descriptor),inner);
    }
    private void addNodeHierarchy(JPanel parent, List<AdrComponentDescriptor> child, boolean inner){
        child.forEach(it -> {
            JPanel viewOf = this.renderer.getViewOf(it,inner);
            switch (it.getType()) {
                case GROUP: {
                    this.addNodeHierarchy(viewOf, ((AdrGroupDescriptor) it).getCells(), true);
                    this.container.add(this.componentsFactory.wrapToSlide(viewOf, 2, 4, 2, 4));
                    break;
                }
                default: {
                    parent.add(this.componentsFactory.wrapToSlide(viewOf, 2, 4, 2, 4));
                }
            }
        });
    }

    private void initTree(JPanel parent, List<AdrComponentDescriptor> child, boolean inner) {
        this.addNodeHierarchy(parent,child,inner);
    }
}
