package com.mercury.platform.ui.adr.components;

import com.mercury.platform.shared.config.descriptor.adr.*;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.adr.components.panel.AdrComponentPanel;
import com.mercury.platform.ui.adr.components.panel.AdrCellPanel;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class AdrGroupFrame extends AbstractAdrComponentFrame<AdrGroupDescriptor> {
    private List<AdrComponentPanel> cells;

    private JPanel cellsPanel;
    public AdrGroupFrame(@NonNull AdrGroupDescriptor descriptor) {
        super(descriptor);
        this.cells = new ArrayList<>();
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.cellsPanel = this.getCellsPanel();
        this.add(this.cellsPanel,BorderLayout.CENTER);
        this.pack();
    }

    private JPanel getCellsPanel(){
        int cellCount = descriptor.getCells().size();
        GridLayout layout = new GridLayout(cellCount, 1);
        if(this.descriptor.getOrientation().equals(AdrComponentOrientation.HORIZONTAL)){
            layout = new GridLayout(1,cellCount);
        }
        JPanel root = componentsFactory.getTransparentPanel(layout);
        descriptor.getCells().forEach(component -> {
                AdrCellPanel adrCellPanel = new AdrCellPanel((AdrDurationComponentDescriptor) component, this.componentsFactory);
                root.add(adrCellPanel);
                this.cells.add(adrCellPanel);
        });
        return root;
    }
    @Override
    public void subscribe() {
        super.subscribe();
        MercuryStoreUI.adrReloadSubject.subscribe(descriptor -> {
            if(descriptor.equals(this.descriptor)){
                int cellCount = this.descriptor.getCells().size();
                switch (this.descriptor.getOrientation()){
                    case VERTICAL:{
                        if(this.descriptor.getGroupType().equals(AdrGroupType.STATIC)) {
                            this.cellsPanel.setLayout(new GridLayout(cellCount, 1));
                        }else {
                            this.cellsPanel.setLayout(new BoxLayout(this.cellsPanel,BoxLayout.Y_AXIS));
                        }
                        break;
                    }
                    case HORIZONTAL:{
                        if(this.descriptor.getGroupType().equals(AdrGroupType.STATIC)) {
                            this.cellsPanel.setLayout(new GridLayout(1,cellCount));
                        }else {
                            this.cellsPanel.setLayout(new BoxLayout(this.cellsPanel,BoxLayout.X_AXIS));
                        }
                        break;
                    }
                }
                this.cells.forEach(item -> {
                    item.setPreferredSize(this.descriptor.getSize());
                    item.setLocation(this.descriptor.getLocation());
                });
                this.setOpacity(this.descriptor.getOpacity());
                this.repaint();
                this.pack();
            }
        });
    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        cells.forEach(it -> {
            it.enableSettings();
            it.setBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.BORDER));
            it.addMouseListener(this.mouseListener);
            it.addMouseListener(this.mouseOverListener);
            it.addMouseMotionListener(this.motionListener);
        });
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        cells.forEach(it -> {
            it.disableSettings();
            it.removeMouseListener(this.mouseListener);
            it.removeMouseMotionListener(this.motionListener);
            it.removeMouseListener(this.mouseOverListener);
            it.setBorder(BorderFactory.createEmptyBorder(0,0,1,0));
        });
        this.pack();
        this.repaint();
    }
}
