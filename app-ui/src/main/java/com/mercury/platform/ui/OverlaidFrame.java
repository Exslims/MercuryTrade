package com.mercury.platform.ui;


import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 26.12.2016.
 */
public abstract class OverlaidFrame extends JFrame implements HasEventHandlers {
    private final int HIDE_TIME = 200;
    private final int SHOW_TIME = 150;

    protected int x;
    protected int y;
    private Timeline hideAnimation;
    private Timeline showAnimation;
    private HideEffectListener hideEffectListener = new HideEffectListener();
    protected ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    protected ConfigManager configManager = ConfigManager.INSTANCE;
    protected OverlaidFrame(String title){
        super(title);
        init();
    }
    protected void init(){
        getRootPane().setOpaque(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(AppThemeColor.FRAME);
        setOpacity(0.2f);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setFocusable(false);

        initHandlers();
        initAnimationTimers();
        this.addMouseListener(hideEffectListener);

        this.setLocation((Point) configManager.getProperty(this.getClass().getSimpleName()));
        this.getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER,1));

    }
    protected void disableHideEffect(){
        this.setOpacity(0.9f);
        this.removeMouseListener(hideEffectListener);
    }
    protected void enableHideEffect(){
        this.addMouseListener(hideEffectListener);
    }
    private boolean isMouseWithInFrame(){
        return this.getBounds().contains(MouseInfo.getPointerInfo().getLocation());
    }
    private void initAnimationTimers(){
        showAnimation = new Timeline(this);
        showAnimation.setDuration(SHOW_TIME);
        showAnimation.addPropertyToInterpolate("opacity", 0.2f, 0.9f);

        hideAnimation = new Timeline(this);
        hideAnimation.setDuration(HIDE_TIME);
        hideAnimation.addPropertyToInterpolate("opacity",0.9f,0.2f);
    }
    private class HideEffectListener extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            OverlaidFrame.this.repaint();
            if(OverlaidFrame.this.getOpacity() < 0.9f) {
                hideAnimation.abort();
                showAnimation.play();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!isMouseWithInFrame()){
                showAnimation.abort();
                hideAnimation.play();
            }
        }
    }
}
