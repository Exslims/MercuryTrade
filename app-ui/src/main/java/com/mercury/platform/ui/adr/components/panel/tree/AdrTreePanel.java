package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.experimental.PerformanceHelper;
import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.ui.adr.components.panel.ui.MercuryLoading;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
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
    private SwingWorker<Void,Void> worker;
    private MercuryLoading mercuryLoading;
    private JScrollPane verticalContainer;
    public AdrTreePanel(List<AdrComponentDescriptor> descriptors, AdrTreeNodeRenderer renderer) {
        super(new BorderLayout());
        performanceHelper.reset();
        this.renderer = renderer;
        this.setBackground(AppThemeColor.FRAME_RGB);
        this.setPreferredSize(new Dimension(280,10));
        this.descriptors = descriptors;
        this.container = new VerticalScrollContainer();
        this.container.setBackground(AppThemeColor.FRAME_RGB);
        this.container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        this.verticalContainer = this.componentsFactory.getVerticalContainer(container);

        this.mercuryLoading = new MercuryLoading();
        mercuryLoading.playLoop();
        this.add(mercuryLoading,BorderLayout.CENTER);
    }
    public void updateTree(){
        if(this.worker != null && !this.worker.isDone()) {
            return;
        }
        this.remove(this.verticalContainer);
        this.mercuryLoading.playLoop();
        this.add(this.mercuryLoading,BorderLayout.CENTER);
        this.worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                updateTree(descriptors);
                return null;
            }

            @Override
            protected void done() {
                mercuryLoading.abort();
                remove(mercuryLoading);
                add(verticalContainer,BorderLayout.CENTER);
                MercuryStoreUI.adrUpdateTree.onNext(true);
            }
        };
        this.worker.execute();
    }
    private void updateTree(List<AdrComponentDescriptor> descriptors){
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
                    this.addNodeHierarchy(viewOf, ((AdrTrackerGroupDescriptor) it).getCells(), true);
                    parent.add(this.componentsFactory.wrapToSlide(viewOf, 2, 4, 2, 4));
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
