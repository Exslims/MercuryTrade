package com.mercury.platform.ui.frame;

import com.mercury.platform.ui.frame.location.UndecoratedFrameState;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 22.01.2017.
 */
public abstract class MovableComponentFrame extends ComponentFrame {
    protected Container mainContainer;
    protected UndecoratedFrameState undecoratedFrameState;
    protected MovableComponentFrame(String title) {
        super(title);
        mainContainer = this.getContentPane();
        undecoratedFrameState = UndecoratedFrameState.DEFAULT;
    }
    protected abstract JPanel panelWhenMove();
    protected abstract int getMinComponentCount();
    public void setState(UndecoratedFrameState state){
        switch (state){
            case MOVING:{
                if(undecoratedFrameState.equals(UndecoratedFrameState.DEFAULT)) {
                    JPanel panel = setUpMoveListeners(panelWhenMove());
                    if(mainContainer.getHeight() < 40) {
                        panel.setPreferredSize(new Dimension(200, 100));
                    }else {
                        panel.setPreferredSize(mainContainer.getSize());
                    }
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
                if(mainContainer.getComponentCount() <= getMinComponentCount()){ //todo
                    this.setVisible(false);
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
                panel.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
                MovableComponentFrame.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBorder(null);
                MovableComponentFrame.this.repaint();
            }
        });
        return panel;
    }
}
