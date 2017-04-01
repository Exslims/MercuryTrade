package com.mercury.platform.ui.frame;

import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.ui.frame.movable.TaskBarFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.manager.HideSettingsManager;
import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public abstract class ComponentFrame extends OverlaidFrame{
    private final int HIDE_TIME = 200;
    private final int SHOW_TIME = 150;
    private final int BORDER_THICKNESS = 1;
    private int HIDE_DELAY = 1000;
    private float minOpacity = 1f;
    private float maxOpacity = 1f;

    protected int x;
    protected int y;
    protected boolean EResizeSpace = false;
    protected boolean SEResizeSpace = false;

    private Timeline hideAnimation;
    private Timeline showAnimation;
    private Timer hideTimer;
    private HideEffectListener hideEffectListener;
    private boolean hideAnimationEnable = false;

    protected boolean processSEResize = true;
    protected boolean processEResize = true;
    protected boolean processHideEffect = true;

    protected ComponentFrame(String title) {
        super(title);
    }

    @Override
    protected void initialize(){
        initAnimationTimers();
        this.setVisible(false);

        this.addMouseListener(new ResizeByWidthMouseListener());
        this.addMouseMotionListener(new ResizeByWidthMouseMotionListener());
        HideSettingsManager.INSTANCE.registerFrame(this);

        FrameSettings frameSettings = configManager.getFrameSettings(this.getClass().getSimpleName());
        if(frameSettings != null) {
            this.setLocation(frameSettings.getFrameLocation());
            this.setSize(frameSettings.getFrameSize());
            this.setMinimumSize(frameSettings.getFrameSize());
            this.setMaximumSize(frameSettings.getFrameSize());
        }
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, BORDER_THICKNESS)));
    }
    protected Point getFrameLocation(){
        return this.getLocationOnScreen();
    }

    public void disableHideEffect(){
        if(processHideEffect) {
            this.setOpacity(maxOpacity);
            this.hideAnimationEnable = false;
            if (hideEffectListener != null) {
                this.removeMouseListener(hideEffectListener);
            }
        }
    }
    public void enableHideEffect(int delay, int minOpacity, int maxOpacity){
        if(processHideEffect) {
            this.HIDE_DELAY = delay * 1000;
            this.minOpacity = minOpacity / 100f;
            this.maxOpacity = maxOpacity / 100f;
            //this.setOpacity(minOpacity / 100f);
            this.setOpacity(maxOpacity / 100f);
            hideEffectListener = new HideEffectListener();
            initAnimationTimers();
            this.addMouseListener(hideEffectListener);
            this.hideAnimationEnable = true;
        }
    }

    private void initAnimationTimers(){
        showAnimation = new Timeline(this);
        showAnimation.setDuration(SHOW_TIME);
        showAnimation.addPropertyToInterpolate("opacity", minOpacity, maxOpacity);

        hideAnimation = new Timeline(this);
        hideAnimation.setDuration(HIDE_TIME);
        hideAnimation.addPropertyToInterpolate("opacity",maxOpacity,minOpacity);
    }
    public void onLocationChange(Point location){
        configManager.saveFrameLocation(this.getClass().getSimpleName(),location);
    }
    protected void onFrameDragged(Point location){
        ComponentFrame.this.setLocation(location);
    }

    private class ResizeByWidthMouseMotionListener extends MouseMotionAdapter{
        @Override
        public void mouseDragged(MouseEvent e) {
            if(EResizeSpace) {
                Point frameLocation = ComponentFrame.this.getLocation();
                ComponentFrame.this.setSize(new Dimension(e.getLocationOnScreen().x - frameLocation.x, ComponentFrame.this.getHeight()));
            }else if(SEResizeSpace){
                Point frameLocation = ComponentFrame.this.getLocation();
                ComponentFrame.this.setSize(new Dimension(e.getLocationOnScreen().x - frameLocation.x, e.getLocationOnScreen().y - frameLocation.y));
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            int frameWidth = ComponentFrame.this.getWidth();
            int frameHeight = ComponentFrame.this.getHeight();
            Point frameLocation = ComponentFrame.this.getLocation();
            Rectangle ERect = new Rectangle(
                    frameLocation.x + frameWidth - (BORDER_THICKNESS + 8),
                    frameLocation.y,BORDER_THICKNESS+8,frameHeight);
            Rectangle SERect = new Rectangle(
                    frameLocation.x + frameWidth - (BORDER_THICKNESS + 8),
                    frameLocation.y + frameHeight - (BORDER_THICKNESS + 8),BORDER_THICKNESS+8,8);

            if(processEResize && ERect.getBounds().contains(e.getLocationOnScreen())) {
                if(processSEResize && SERect.getBounds().contains(e.getLocationOnScreen())){
                    ComponentFrame.this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                    SEResizeSpace = true;
                    EResizeSpace = false;
                }else {
                    ComponentFrame.this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                    EResizeSpace = true;
                    SEResizeSpace = false;
                }
            }else {
                EResizeSpace = false;
                SEResizeSpace = false;
                ComponentFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
    private class ResizeByWidthMouseListener extends MouseAdapter{
        @Override
        public void mouseReleased(MouseEvent e) {
            if(hideAnimationEnable && !isMouseWithInFrame()) {
                hideTimer.start();
            }
            Dimension size = ComponentFrame.this.getSize();
            if(EResizeSpace){
                ComponentFrame.this.setMaximumSize(size);
                ComponentFrame.this.setMinimumSize(size);
                if(ComponentFrame.this.getClass().getSimpleName().equals("IncMessageFrame")){
                    configManager.saveFrameSize(ComponentFrame.this.getClass().getSimpleName(),new Dimension(size.width,0));
                }else {
                    configManager.saveFrameSize(ComponentFrame.this.getClass().getSimpleName(), size);
                }
            }else if(SEResizeSpace){
                ComponentFrame.this.setMinimumSize(size);
                ComponentFrame.this.setMaximumSize(size);
                configManager.saveFrameSize(ComponentFrame.this.getClass().getSimpleName(),ComponentFrame.this.getSize());
            }
            EResizeSpace = false;
            SEResizeSpace = false;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ComponentFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(EResizeSpace || SEResizeSpace) {
                Dimension size = configManager.getMinimumFrameSize(ComponentFrame.this.getClass().getSimpleName());
                ComponentFrame.this.setMinimumSize(size);
            }
        }
    }

    /**
     * Listeners for hide&show animation on frame
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
            ComponentFrame.this.repaint();
            hideTimer.stop();
            if(ComponentFrame.this.getOpacity() < maxOpacity) {
                showAnimation.play();
            }
        }

        // Если я убираю !isMouseWithInFrame он 100% всегда фейдится, но он также фейдится если я маусовлю по кнопке
        @Override
        public void mouseExited(MouseEvent e) {
            if(!isMouseWithInFrame() && !EResizeSpace){ // NEEDS WORK
                hideTimer.stop();
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
            e.translatePoint(ComponentFrame.this.getLocation().x - x,ComponentFrame.this.getLocation().y - y);
            onFrameDragged(new Point(e.getX(),e.getY()));
        }
    }
    public class DraggedFrameMouseListener extends MouseAdapter{
        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            if(getLocationOnScreen().y + getSize().height > dimension.height){
                setLocation(getLocationOnScreen().x, dimension.height - getSize().height);
            }
            onLocationChange(getLocation());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        return new Dimension(this.getMaximumSize().width,preferredSize.height);
    }
}
