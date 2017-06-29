package com.mercury.platform.ui.frame;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.impl.FramesConfigurationService;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.frame.other.MercuryLoadingFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractOverlaidFrame extends JFrame implements AsSubscriber {
    private final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());
    protected FrameVisibleState prevState;
    protected boolean processingHideEvent = true;

    protected ComponentsFactory componentsFactory = new ComponentsFactory();
    protected ConfigManager configManager = ConfigManager.INSTANCE;
    protected FramesConfigurationService framesService;

    protected LayoutManager layout;
    protected AbstractOverlaidFrame(){
        super("MercuryTrade");
        if(!this.getClass().equals(MercuryLoadingFrame.class)) {
            this.framesService = Configuration.get().framesConfiguration();
        }
        this.getRootPane().setOpaque(false);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(AppThemeColor.FRAME);
        this.setFocusableWindowState(false);
        this.setFocusable(false);
        this.setAlwaysOnTop(true);
        this.setVisible(false);
        this.prevState = FrameVisibleState.HIDE;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                AbstractOverlaidFrame.this.repaint();
            }
        });

        MercuryStoreCore.INSTANCE.frameVisibleSubject.subscribe(this::changeVisible);
    }
    protected void changeVisible(FrameVisibleState state){
        if (this.processingHideEvent) {
            switch (state) {
                case SHOW: {
                    if (this.prevState.equals(FrameVisibleState.SHOW)) {
                        this.showComponent();
                    }
                }
                break;
                case HIDE: {
                    if (AbstractOverlaidFrame.this.isVisible()) {
                        this.prevState = FrameVisibleState.SHOW;
                    }else {
                        this.prevState = FrameVisibleState.HIDE;
                    }
                    this.hideComponent();
                }
                break;
            }
        }
    }
    public void init(){
        this.layout = getFrameLayout();
        this.setLayout(layout);
        this.initialize();
        this.subscribe();
    }

    protected abstract LayoutManager getFrameLayout();
    protected abstract void initialize();

    protected boolean isMouseWithInFrame(){
        return this.getBounds().contains(MouseInfo.getPointerInfo().getLocation());
    }
    public void showComponent(){
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }
    public void hideComponent(){
        this.setVisible(false);
    }

    public void setPrevState(FrameVisibleState prevState) {
        this.prevState = prevState;
    }
}
