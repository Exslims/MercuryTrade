package com.mercury.platform.ui.frame;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.AsSubscriber;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
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

    protected ComponentsFactory componentsFactory;
    protected ConfigManager configManager = ConfigManager.INSTANCE;

    protected LayoutManager layout;
    protected AbstractOverlaidFrame(String title){
        super(title);
        this.componentsFactory = new ComponentsFactory();
        getRootPane().setOpaque(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(AppThemeColor.FRAME);
        setFocusableWindowState(false);
        setFocusable(false);
        setAlwaysOnTop(true);
        setVisible(false);
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
        if (processingHideEvent) {
            switch (state) {
                case SHOW: {
                    if (prevState.equals(FrameVisibleState.SHOW)) {
                        showComponent();
                    }
                }
                break;
                case HIDE: {
                    if (AbstractOverlaidFrame.this.isVisible()) {
                        prevState = FrameVisibleState.SHOW;
                    }else {
                        prevState = FrameVisibleState.HIDE;
                    }
                    hideComponent();
                }
                break;
            }
        }
    }
    public void init(){
        this.layout = getFrameLayout();
        setLayout(layout);
        initialize();
        subscribe();
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
