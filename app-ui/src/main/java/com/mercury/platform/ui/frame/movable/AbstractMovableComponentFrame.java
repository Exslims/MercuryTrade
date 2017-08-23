package com.mercury.platform.ui.frame.movable;

import com.mercury.platform.ui.frame.AbstractScalableComponentFrame;
import com.mercury.platform.ui.frame.setup.location.LocationState;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractMovableComponentFrame extends AbstractScalableComponentFrame {
    protected boolean locationWasChanged = false;
    protected boolean inMoveMode = false;
    protected boolean enableMouseOverBorder = true;
    private MovableFrameConstraints prevConstraints;
    private LocationState moveState = LocationState.DEFAULT;

    protected AbstractMovableComponentFrame() {
        super();
    }

    protected void onUnlock() {
        this.pack();
        this.repaint();
    }

    protected void onLock() {
        this.repaint();
        this.pack();
    }

    protected abstract JPanel getPanelForPINSettings();

    public void setState(LocationState state) {
        switch (state) {
            case MOVING: {
                this.moveState = LocationState.MOVING;
                inMoveMode = true;
                this.prevConstraints = new MovableFrameConstraints(
                        this.processSEResize,
                        this.processEResize,
                        this.isVisible(),
                        this.getBackground(),
                        this.getRootPane().getBorder(),
                        this.getLocation()
                );
                this.processEResize = false;
                this.processSEResize = false;
                JPanel panelForPin = setUpMoveListeners(getPanelForPINSettings());
                this.setBackground(panelForPin.getBackground());
                this.setLocation(this.framesConfig.get(this.getClass().getSimpleName()).getFrameLocation());
                this.getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                this.setContentPane(panelForPin);
                this.setVisible(true);
                this.setAlwaysOnTop(true);
                this.onUnlock();
                break;
            }
            case DEFAULT: {
                this.moveState = LocationState.DEFAULT;
                this.setContentPane(mainContainer);
                this.processSEResize = prevConstraints.processSEResize;
                this.processEResize = prevConstraints.processEResize;
                this.setVisible(prevConstraints.visible);
                this.setBackground(prevConstraints.bgColor);
                this.getRootPane().setBorder(prevConstraints.border);
                this.setLocation(prevConstraints.location);
                inMoveMode = false;
                this.onLock();
                break;
            }
        }
    }

    @Override
    public void onLocationChange(Point location) {
        super.onLocationChange(location);
        if (inMoveMode) {
            prevConstraints.location = location;
            locationWasChanged = true;
        }
    }

    private JPanel setUpMoveListeners(JPanel panel) {
        panel.addMouseMotionListener(new DraggedFrameMotionListener());
        panel.addMouseListener(new DraggedFrameMouseListener());

        if (this.enableMouseOverBorder) {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                    getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE, 1));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    if (!AbstractMovableComponentFrame.this.getBounds().contains(e.getPoint())) {
                        getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                    }
                }
            });
        }
        return panel;
    }

    public LocationState getMoveState() {
        return moveState;
    }

    protected class MovableFrameConstraints {
        private boolean processSEResize;
        private boolean processEResize;
        private boolean visible;
        private Color bgColor;
        private Border border;
        private Point location;

        MovableFrameConstraints(boolean processSEResize,
                                boolean processEResize,
                                boolean visible,
                                Color bgColor,
                                Border border,
                                Point location) {
            this.processSEResize = processSEResize;
            this.processEResize = processEResize;
            this.visible = visible;
            this.bgColor = bgColor;
            this.border = border;
            this.location = location;
        }

        public Point getLocation() {
            return location;
        }

        public void setLocation(Point location) {
            this.location = location;
        }
    }
}
