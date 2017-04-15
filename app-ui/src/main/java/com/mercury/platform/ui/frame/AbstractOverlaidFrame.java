package com.mercury.platform.ui.frame;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class AbstractOverlaidFrame extends JFrame implements HasEventHandlers{
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

        MercuryStore.INSTANCE.frameVisibleSubject.subscribe(state -> {
            if(!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(()-> changeVisible(state));
            }else {
                changeVisible(state);
            }
        });
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
        initHandlers();
    }

    protected abstract LayoutManager getFrameLayout();
    protected abstract void initialize();

    protected boolean isMouseWithInFrame(){
        return this.getBounds().contains(MouseInfo.getPointerInfo().getLocation());
    }
    public void showComponent(){
        this.setVisible(true);
    }
    public void hideComponent(){
        this.setVisible(false);
    }

    public void setPrevState(FrameVisibleState prevState) {
        this.prevState = prevState;
    }
}
