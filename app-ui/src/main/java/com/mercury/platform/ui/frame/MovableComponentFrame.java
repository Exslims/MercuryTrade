package com.mercury.platform.ui.frame;

import com.mercury.platform.ui.frame.location.UndecoratedFrameState;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 22.01.2017.
 */
public abstract class MovableComponentFrame extends ComponentFrame {
    protected Container mainContainer;
    private boolean wasVisible;
    private Color prevBGColor;
    private Border prevBorder;
    protected UndecoratedFrameState undecoratedFrameState;
    protected MovableComponentFrame(String title) {
        super(title);
        mainContainer = this.getContentPane();
        undecoratedFrameState = UndecoratedFrameState.DEFAULT;
    }
    protected abstract JPanel panelWhenMove();
    public void setState(UndecoratedFrameState state){
        switch (state){
            case MOVING:{
                if(undecoratedFrameState.equals(UndecoratedFrameState.DEFAULT)) {
                    wasVisible = this.isVisible();
                    prevBGColor = this.getBackground();
                    prevBorder = this.getRootPane().getBorder();
                    JPanel panel = setUpMoveListeners(panelWhenMove());
                    if(mainContainer.getHeight() < 100 && !this.getClass().getSimpleName().equals("TaskBarFrame")){
                        panel.setPreferredSize(new Dimension(200, 100));
                    }else {
                        panel.setPreferredSize(mainContainer.getSize());
                    }
                    this.setBackground(AppThemeColor.FRAME);
                    this.getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                    this.setContentPane(panel);
                    this.setVisible(true);
                    this.setAlwaysOnTop(true);
                    this.pack();
                    this.repaint();
                    undecoratedFrameState = UndecoratedFrameState.MOVING;
                }
                break;
            }
            case DEFAULT: {
                this.setContentPane(mainContainer);
                this.setBackground(prevBGColor);
                this.getRootPane().setBorder(prevBorder);
                this.setVisible(wasVisible);
                if(mainContainer.getComponentCount() > 0 && this.getClass().getSimpleName().equals("IncMessageFrame")){
                    this.setVisible(true);
                }
                this.setPreferredSize(null);
                this.pack();
                this.repaint();
                undecoratedFrameState = UndecoratedFrameState.DEFAULT;
                break;
            }
        }
    }
    private JPanel setUpMoveListeners(JPanel panel){
        panel.addMouseMotionListener(new DraggedFrameMotionListener());
        panel.addMouseListener(new DraggedFrameMouseListener());

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE, 1));
                MovableComponentFrame.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                MovableComponentFrame.this.repaint();
            }
        });
        return panel;
    }
}
