package com.mercury.platform.ui.frame;

import com.mercury.platform.ui.frame.location.UndecoratedFrameState;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class MovableComponentFrame extends ComponentFrame {
    protected Container mainContainer;
    private FrameConstraints prevConstraints;
    protected boolean locationWasChanged = false;
    protected boolean sizeWasChanged = false;
    protected boolean inMoveMode = false;
    protected boolean enableMouseOverBorder = true;
    protected MovableComponentFrame(String title) {
        super(title);
        mainContainer = this.getContentPane();
    }
    protected abstract JPanel panelWhenMove();
    protected void onUnlock(){
        this.pack();
        this.repaint();
    }
    protected void onLock(){
        this.repaint();
        this.pack();
    }
    public void setState(UndecoratedFrameState state){
        switch (state){
            case MOVING:{
                inMoveMode = true;
                this.prevConstraints = new FrameConstraints(
                        this.processSEResize,
                        this.processEResize,
                        this.isVisible(),
                        this.getBackground(),
                        this.getRootPane().getBorder(),
                        this.getLocation(),
                        this.getMinimumSize(),
                        this.getMaximumSize()
                );
                JPanel panel = setUpMoveListeners(panelWhenMove());
                this.processEResize = false;
                this.processSEResize = false;
                this.setBackground(panel.getBackground());
                this.setLocation(configManager.getFrameSettings(this.getClass().getSimpleName()).getFrameLocation());
                this.getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                this.setMinimumSize(null);
                this.setContentPane(panel);
                this.setVisible(true);
                this.setAlwaysOnTop(true);
                this.onUnlock();
                break;
            }
            case DEFAULT: {
                this.setContentPane(mainContainer);
                this.processSEResize = prevConstraints.processSEResize;
                this.processEResize = prevConstraints.processEResize;
                this.setVisible(prevConstraints.visible);
                this.setBackground(prevConstraints.bgColor);
                this.getRootPane().setBorder(prevConstraints.border);
                this.setLocation(prevConstraints.location);
                if(sizeWasChanged){
                    this.setPreferredSize(this.getSize());
                    this.setMinimumSize(this.getSize());
                    this.setMaximumSize(this.getSize());
                    sizeWasChanged = false;
                }else {
                    this.setMinimumSize(prevConstraints.minSize);
                    this.setMaximumSize(prevConstraints.maxSize);
                }
                inMoveMode = false;
                this.onLock();
                break;
            }
        }
    }

    @Override
    public void onLocationChange(Point location) {
        super.onLocationChange(location);
        prevConstraints.location = location;
        locationWasChanged = true;
    }

    private JPanel setUpMoveListeners(JPanel panel){
        panel.addMouseMotionListener(new DraggedFrameMotionListener());
        panel.addMouseListener(new DraggedFrameMouseListener());

        if(enableMouseOverBorder) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE, 1));
                    MovableComponentFrame.this.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!panel.getBounds().contains(e.getPoint())) {
                        getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                    }
                    MovableComponentFrame.this.repaint();
                }
            });
        }
        return panel;
    }
    protected class FrameConstraints {
        private boolean processSEResize;
        private boolean processEResize;
        private boolean visible;
        private Color bgColor;
        private Border border;
        private Point location;
        private Dimension minSize;
        private Dimension maxSize;

        FrameConstraints(boolean processSEResize,
                         boolean processEResize,
                         boolean visible,
                         Color bgColor,
                         Border border,
                         Point location,
                         Dimension minSize,
                         Dimension maxSize) {
            this.processSEResize = processSEResize;
            this.processEResize = processEResize;
            this.visible = visible;
            this.bgColor = bgColor;
            this.border = border;
            this.location = location;
            this.minSize = minSize;
            this.maxSize = maxSize;
        }

        public void setLocation(Point location) {
            this.location = location;
        }

        public Point getLocation() {
            return location;
        }
    }
}
