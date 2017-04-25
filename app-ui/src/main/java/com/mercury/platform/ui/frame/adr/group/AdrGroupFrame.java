package com.mercury.platform.ui.frame.adr.group;

import com.mercury.platform.shared.entity.adr.AdrGroupDescriptor;
import com.mercury.platform.ui.components.panel.adr.group.AdrGroupCellPanel;
import com.mercury.platform.ui.frame.adr.AbstractAdrFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class AdrGroupFrame extends AbstractAdrFrame {
    private List<AdrGroupCellPanel> cells;
    private AdrGroupDescriptor descriptor;

    private int x;
    private int y;

    private DraggedFrameMouseListener mouseListener;
    private DraggedFrameMotionListener motionListener;
    private MouseAdapter mouseOverListener;

    public AdrGroupFrame(@NonNull AdrGroupDescriptor descriptor) {
        super("MercuryTrade");
        this.descriptor = descriptor;
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
        this.add(getCellsPanel(),BorderLayout.CENTER);
        this.pack();
    }

    private JPanel getCellsPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(4, 1));
        descriptor.getCells().forEach(cellDescriptor -> {
            AdrGroupCellPanel adrGroupCellPanel = new AdrGroupCellPanel(cellDescriptor,this.componentsFactory);
            root.add(adrGroupCellPanel);
            cells.add(adrGroupCellPanel);
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
    public void initHandlers() {

    }

    @Override
    public void enableSettings() {
        super.enableSettings();
        this.setBackground(AppThemeColor.FRAME);
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(1,1,0,1,AppThemeColor.BORDER));
        cells.forEach(it -> {
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
                AdrGroupFrame.this.setLocation(e.getPoint());
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
            }
        }
    }
}
