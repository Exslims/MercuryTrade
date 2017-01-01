package com.mercury.platform.ui.frame;


import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.HasEventHandlers;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.SCEventHandler;
import com.mercury.platform.shared.events.custom.ChangeFrameVisibleEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by Константин on 26.12.2016.
 */
public abstract class OverlaidFrame extends JFrame implements HasEventHandlers {
    private final int HIDE_TIME = 200;
    private final int SHOW_TIME = 150;
    private final int BORDER_THICKNESS = 1;
    private final int HIDE_DELAY = 1000;

    protected int x;
    protected int y;
    private boolean withinResizeSpace = false;

    private Timeline hideAnimation;
    private Timeline showAnimation;
    private Timer hideTimer;
    private HideEffectListener hideEffectListener = new HideEffectListener();
    private boolean hideAnimationEnable = true;

    protected FrameStates prevState;

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
        setLayout(getFrameLayout());

        initHandlers();
        initAnimationTimers();
        this.addMouseListener(hideEffectListener);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                OverlaidFrame.this.repaint();
            }
        });
        this.addMouseListener(new ResizeMouseListener());
        this.addMouseMotionListener(new ResizeMouseMotionListener());

        if(!this.getClass().getSimpleName().equals("NotificationFrame")) {
            FrameSettings frameSettings = configManager.getFrameSettings(this.getClass().getSimpleName());
            this.setLocation(frameSettings.getFrameLocation());
            this.setMinimumSize(new Dimension(frameSettings.getFrameSize().width,0));
        }
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, BORDER_THICKNESS)));

        EventRouter.registerHandler(ChangeFrameVisibleEvent.class, new SCEventHandler<ChangeFrameVisibleEvent>() {
            @Override
            public void handle(ChangeFrameVisibleEvent event) {
                if(OverlaidFrame.this.getClass().getSimpleName().equals("MessageFrame")){
                    if(prevState != null) {
                        System.out.println("PREV: " + prevState.toString());
                    }
                    System.out.println("INVOKE: " + event.getStates().toString());
                }
                switch (event.getStates()){
                    case SHOW:{
                        if(prevState == null){
                            prevState = FrameStates.SHOW;
                        }
                        if(prevState.equals(FrameStates.SHOW)){
                            OverlaidFrame.this.setVisible(true);
                        }
                    }
                    break;
                    case HIDE:{
                        if(OverlaidFrame.this.getClass().getSimpleName().equals("MessageFrame")){
                            System.out.println(OverlaidFrame.this.isShowing());
                        }
                        if(!OverlaidFrame.this.isShowing()){
                            prevState = FrameStates.HIDE;
                        }else {
                            prevState = FrameStates.SHOW;
                        }
                        OverlaidFrame.this.setVisible(false);
                    }
                    break;
                }
            }
        });

    }
    protected void disableHideEffect(){
        this.setOpacity(0.9f);
        this.hideAnimationEnable = false;
        this.removeMouseListener(hideEffectListener);
    }
    protected void enableHideEffect(){
        this.addMouseListener(hideEffectListener);
        this.hideAnimationEnable = true;
    }
    protected abstract LayoutManager getFrameLayout();
    protected boolean isMouseWithInFrame(){
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
    private class ResizeMouseMotionListener extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e) {
            Point frameLocation = OverlaidFrame.this.getLocation();
            OverlaidFrame.this.setSize(new Dimension(e.getLocationOnScreen().x - frameLocation.x,OverlaidFrame.this.getHeight()));
        }
    }
    private class ResizeMouseListener extends MouseAdapter{
        private Rectangle rightResizeRect = new Rectangle();
        @Override
        public void mousePressed(MouseEvent e) {
            withinResizeSpace = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            withinResizeSpace = false;
            if(hideAnimationEnable && !isMouseWithInFrame()) {
                hideTimer.start();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            int frameWidth = OverlaidFrame.this.getWidth();
            int frameHeight = OverlaidFrame.this.getHeight();
            Point frameLocation = OverlaidFrame.this.getLocation();
            rightResizeRect = new Rectangle(
                    frameLocation.x + frameWidth - (BORDER_THICKNESS + 2),
                    frameLocation.y,BORDER_THICKNESS+2,frameHeight);
            if(rightResizeRect.getBounds().contains(e.getLocationOnScreen())) {
                OverlaidFrame.this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            OverlaidFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Listeners for hide&show animation on frame.
     */
    private class HideEffectListener extends MouseAdapter {
        public HideEffectListener(){
            hideTimer = new Timer(HIDE_DELAY,listener ->{
                showAnimation.abort();
                hideAnimation.play();
            });
            hideTimer.setRepeats(false);
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            OverlaidFrame.this.repaint();
            hideTimer.stop();
            if(OverlaidFrame.this.getOpacity() < 0.9f) {
                showAnimation.play();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(!isMouseWithInFrame() && !withinResizeSpace){
                hideTimer.start();
            }
        }
    }

    /**
     * Frame Listeners to change the position on the screen.
     */
    public class DraggedFrameMotionListener extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            e.translatePoint(OverlaidFrame.this.getLocation().x - x,OverlaidFrame.this.getLocation().y - y);
            OverlaidFrame.this.setLocation(e.getX(),e.getY());
            configManager.saveFrameLocation(OverlaidFrame.this.getClass().getSimpleName(),OverlaidFrame.this.getLocationOnScreen());
        }
    }
    public class DraggedFrameMouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }
    }
}
