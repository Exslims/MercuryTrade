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
    private UndecoratedFrameState undecoratedFrameState;
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
                    this.setContentPane(setUpMoveListeners(panelWhenMove()));
                    if(mainContainer.getHeight() < 10) {
                        this.setPreferredSize(new Dimension(200, 100));
                    }else {
                        this.setPreferredSize(mainContainer.getSize());
                    }
                    this.setVisible(true);
                    this.setAlwaysOnTop(true);
                    this.pack();
                    this.repaint();
                }
                break;
            }
            case DEFAULT: {
                this.setContentPane(mainContainer);
                if(mainContainer.getComponentCount() == 0){
                    this.setVisible(false);
                }
                this.setPreferredSize(null);
                this.packFrame();
                this.repaint();
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
