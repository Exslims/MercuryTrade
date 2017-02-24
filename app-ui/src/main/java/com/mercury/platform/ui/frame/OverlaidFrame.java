package com.mercury.platform.ui.frame;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.MercuryEventHandler;
import com.mercury.platform.shared.events.custom.ChangeFrameVisibleEvent;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 17.01.2017.
 */
public abstract class OverlaidFrame extends JFrame implements HasEventHandlers {
    protected FrameStates prevState;
    protected boolean processingHideEvent = true;

    protected ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    protected ConfigManager configManager = ConfigManager.INSTANCE;

    protected LayoutManager layout;
    protected OverlaidFrame(String title){
        super(title);
        getRootPane().setOpaque(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(AppThemeColor.FRAME);
        setFocusableWindowState(false);
        setFocusable(false);
        setAlwaysOnTop(true);
        setVisible(false);
        this.prevState = FrameStates.HIDE;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                OverlaidFrame.this.repaint();
            }
        });

        EventRouter.INSTANCE.registerHandler(ChangeFrameVisibleEvent.class, new MercuryEventHandler<ChangeFrameVisibleEvent>() {
            @Override
            public void handle(ChangeFrameVisibleEvent event) {
                if(!SwingUtilities.isEventDispatchThread()) {
                    SwingUtilities.invokeLater(()-> changeVisible(event.getStates()));
                }else {
                    changeVisible(event.getStates());
                }
            }
            private void changeVisible(FrameStates state){
                if (processingHideEvent) {
                    switch (state) {
                        case SHOW: {
                            if (prevState.equals(FrameStates.SHOW)) {
                                showComponent();
                            }
                        }
                        break;
                        case HIDE: {
                            if (OverlaidFrame.this.isVisible()) {
                                if (!OverlaidFrame.this.isVisible()) {
                                    prevState = FrameStates.HIDE;
                                } else {
                                    prevState = FrameStates.SHOW;
                                }
                                hideComponent();
                            }
                        }
                        break;
                    }
                }
            }
        });
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
}
