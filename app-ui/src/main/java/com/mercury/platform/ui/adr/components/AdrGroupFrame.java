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


public class AdrGroupFrame extends AbstractAdrFrame<AdrGroupDescriptor> {
    private List<AdrComponentPanel> cells;

    private int x;
    private int y;

    private DraggedFrameMouseListener mouseListener;
    private DraggedFrameMotionListener motionListener;
    private MouseAdapter mouseOverListener;

    private JPanel cellsPanel;
    public AdrGroupFrame(@NonNull AdrGroupDescriptor descriptor) {
        super(descriptor);
        this.cells = new ArrayList<>();
        this.mouseListener = new DraggedFrameMouseListener();
        this.motionListener = new DraggedFrameMotionListener();
        this.mouseOverListener = getMouseOverListener();

    }

    @Override
    protected void initialize() {
        this.setLocation(descriptor.getLocation());
        this.setOpacity(descriptor.getOpacity());
        this.componentsFactory.setScale(descriptor.getScale());
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
    private MouseAdapter getMouseOverListener(){
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getSource()).setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
                repaint();
                pack();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JPanel)e.getSource()).setBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.BORDER));
                repaint();
                pack();
            }
        };
    }
    @Override
    public void subscribe() {
        MercuryStoreUI.adrRepaintSubject.subscribe(state -> {
            this.repaint();
            this.pack();
        });
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
        this.setBackground(AppThemeColor.FRAME);
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(1,1,0,1,AppThemeColor.BORDER));
        cells.forEach(it -> {
            it.enableSettings();
            it.setBorder(BorderFactory.createMatteBorder(0,0,1,0,AppThemeColor.BORDER));
            it.addMouseListener(this.mouseListener);
            it.addMouseListener(this.mouseOverListener);
            it.addMouseMotionListener(this.motionListener);
        });
        this.addMouseListener(this.mouseListener);
        this.addMouseMotionListener(this.motionListener);
    }

    @Override
    public void disableSettings() {
        super.disableSettings();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.getRootPane().setBorder(BorderFactory.createEmptyBorder(1,1,1,1));

        cells.forEach(it -> {
            it.disableSettings();
            it.removeMouseListener(this.mouseListener);
            it.removeMouseMotionListener(this.motionListener);
            it.removeMouseListener(this.mouseOverListener);
            it.setBorder(BorderFactory.createEmptyBorder(0,0,1,0));
        });

        this.removeMouseListener(this.mouseListener);
        this.removeMouseMotionListener(this.motionListener);
        this.pack();
        this.repaint();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    public class DraggedFrameMotionListener extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                e.translatePoint(AdrGroupFrame.this.getLocation().x - x, AdrGroupFrame.this.getLocation().y - y);
                Point point = e.getPoint();
                AdrGroupFrame.this.setLocation(point);
            }
        }
    }
    public class DraggedFrameMouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                x = e.getX();
                y = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)) {
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                if (getLocationOnScreen().y + getSize().height > dimension.height) {
                    setLocation(getLocationOnScreen().x, dimension.height - getSize().height);
                }
                descriptor.setLocation(getLocationOnScreen());
                MercuryStoreUI.adrUpdateSubject.onNext(descriptor);
                MercuryStoreCore.saveConfigSubject.onNext(true);
            }
        }
    }
}
