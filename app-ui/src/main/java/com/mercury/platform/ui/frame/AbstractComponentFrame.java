package com.mercury.platform.ui.frame;

import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.manager.HideSettingsManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public abstract class AbstractComponentFrame extends AbstractOverlaidFrame implements ViewInit {
    private final int HIDE_TIME = 200;
    private final int SHOW_TIME = 150;
    private final int BORDER_THICKNESS = 1;
    protected int x;
    protected int y;
    protected boolean EResizeSpace = false;
    protected boolean SEResizeSpace = false;
    protected boolean processSEResize = true;
    protected boolean processEResize = true;
    protected boolean processHideEffect = true;
    private int HIDE_DELAY = 1000;
    private float minOpacity = 1f;
    private float maxOpacity = 1f;
    private Timeline hideAnimation;
    private Timeline showAnimation;
    private Timer hideTimer;
    private HideEffectListener hideEffectListener;
    private boolean hideAnimationEnable = false;

    protected AbstractComponentFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.initAnimationTimers();
        this.setVisible(false);

        this.addMouseListener(new ResizeByWidthMouseListener());
        this.addMouseMotionListener(new ResizeByWidthMouseMotionListener());
        HideSettingsManager.INSTANCE.registerFrame(this);

        FrameDescriptor frameDescriptor = this.framesConfig.get(this.getClass().getSimpleName());
        if (frameDescriptor != null) {
            this.setLocation(frameDescriptor.getFrameLocation());
            this.setSize(frameDescriptor.getFrameSize());
            this.setMinimumSize(frameDescriptor.getFrameSize());
            this.setMaximumSize(frameDescriptor.getFrameSize());
        }
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, BORDER_THICKNESS)));
    }

    protected Point getFrameLocation() {
        return this.getLocationOnScreen();
    }

    public void disableHideEffect() {
        if (this.processHideEffect) {
            this.setOpacity(maxOpacity);
            this.hideAnimationEnable = false;
            if (this.hideEffectListener != null) {
                this.removeMouseListener(hideEffectListener);
            }
        }
    }

    public void enableHideEffect(int delay, int minOpacity, int maxOpacity) {
        if (processHideEffect) {
            this.HIDE_DELAY = delay * 1000;
            this.minOpacity = minOpacity / 100f;
            this.maxOpacity = maxOpacity / 100f;
            //this.setOpacity(minOpacity / 100f);
            this.setOpacity(maxOpacity / 100f);
            if (this.hideEffectListener != null) {
                this.removeMouseListener(hideEffectListener);
                this.showAnimation.abort();
                this.hideAnimation.abort();
            }
            this.hideEffectListener = new HideEffectListener();
            this.addMouseListener(hideEffectListener);
            this.initAnimationTimers();
            this.hideAnimationEnable = true;
        }
    }

    private void initAnimationTimers() {
        this.showAnimation = new Timeline(this);
        this.showAnimation.setDuration(SHOW_TIME);
        this.showAnimation.addPropertyToInterpolate("opacity", this.minOpacity, this.maxOpacity);

        this.hideAnimation = new Timeline(this);
        this.hideAnimation.setDuration(HIDE_TIME);
        this.hideAnimation.addPropertyToInterpolate("opacity", this.maxOpacity, this.minOpacity);
    }

    public void onLocationChange(Point location) {
        FrameDescriptor frameDescriptor = this.framesConfig.getMap().get(this.getClass().getSimpleName());
        frameDescriptor.setFrameLocation(location);
        MercuryStoreCore.saveConfigSubject.onNext(true);
    }

    public void onSizeChange() {
        MercuryStoreCore.saveConfigSubject.onNext(true);
    }

    protected void onFrameDragged(Point location) {
        this.setLocation(location);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        return new Dimension(this.getMaximumSize().width, preferredSize.height);
    }

    private class ResizeByWidthMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (EResizeSpace) {
                Point frameLocation = AbstractComponentFrame.this.getLocation();
                AbstractComponentFrame.this.setSize(new Dimension(e.getLocationOnScreen().x - frameLocation.x, AbstractComponentFrame.this.getHeight()));
            } else if (SEResizeSpace) {
                Point frameLocation = AbstractComponentFrame.this.getLocation();
                AbstractComponentFrame.this.setSize(new Dimension(e.getLocationOnScreen().x - frameLocation.x, e.getLocationOnScreen().y - frameLocation.y));
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int frameWidth = AbstractComponentFrame.this.getWidth();
            int frameHeight = AbstractComponentFrame.this.getHeight();
            Point frameLocation = AbstractComponentFrame.this.getLocation();
            Rectangle ERect = new Rectangle(
                    frameLocation.x + frameWidth - (BORDER_THICKNESS + 8),
                    frameLocation.y, BORDER_THICKNESS + 8, frameHeight);
            Rectangle SERect = new Rectangle(
                    frameLocation.x + frameWidth - (BORDER_THICKNESS + 8),
                    frameLocation.y + frameHeight - (BORDER_THICKNESS + 8), BORDER_THICKNESS + 8, 8);

            if (processEResize && ERect.getBounds().contains(e.getLocationOnScreen())) {
                if (processSEResize && SERect.getBounds().contains(e.getLocationOnScreen())) {
                    AbstractComponentFrame.this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                    SEResizeSpace = true;
                    EResizeSpace = false;
                } else {
                    AbstractComponentFrame.this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
                    EResizeSpace = true;
                    SEResizeSpace = false;
                }
            } else {
                EResizeSpace = false;
                SEResizeSpace = false;
                AbstractComponentFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    private class ResizeByWidthMouseListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent e) {
            if (hideAnimationEnable && !isMouseWithInFrame()) {
                hideTimer.start();
            }
            Dimension size = AbstractComponentFrame.this.getSize();
            FrameDescriptor frameDescriptor = framesConfig.getMap().get(AbstractComponentFrame.this.getClass().getSimpleName());
            if (EResizeSpace) {
                if (AbstractComponentFrame.this.getClass().getSimpleName().equals("NotificationFrame")) {
                    AbstractComponentFrame.this.setMaximumSize(new Dimension(size.width, 0));
                    AbstractComponentFrame.this.setMinimumSize(new Dimension(size.width, 0));
                    frameDescriptor.setFrameSize(new Dimension(size.width, 0));
                } else {
                    AbstractComponentFrame.this.setMaximumSize(size);
                    AbstractComponentFrame.this.setMinimumSize(size);
                    frameDescriptor.setFrameSize(size);
                }
                onSizeChange();
            } else if (SEResizeSpace) {
                AbstractComponentFrame.this.setMinimumSize(size);
                AbstractComponentFrame.this.setMaximumSize(size);
                frameDescriptor.setFrameSize(AbstractComponentFrame.this.getSize());
                onSizeChange();
            }
            EResizeSpace = false;
            SEResizeSpace = false;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            AbstractComponentFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (EResizeSpace || SEResizeSpace) {
                Dimension size = framesConfig.getMinimumSize(AbstractComponentFrame.this.getClass().getSimpleName());
                AbstractComponentFrame.this.setMinimumSize(size);
            }
        }
    }

    /**
     * Listeners for hide&show animation on frame
     */
    private class HideEffectListener extends MouseAdapter {
        public HideEffectListener() {
            hideTimer = new Timer(HIDE_DELAY, listener -> {
                showAnimation.abort();
                hideAnimation.play();
            });
            hideTimer.setRepeats(false);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            AbstractComponentFrame.this.repaint();
            hideTimer.stop();
            if (AbstractComponentFrame.this.getOpacity() < maxOpacity) {
                showAnimation.play();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!isMouseWithInFrame() && !EResizeSpace) { // NEEDS WORK
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
            e.translatePoint(AbstractComponentFrame.this.getLocation().x - x, AbstractComponentFrame.this.getLocation().y - y);
            onFrameDragged(new Point(e.getX(), e.getY()));
        }
    }

    public class DraggedFrameMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            if (getLocationOnScreen().y + getSize().height > dimension.height) {
                setLocation(getLocationOnScreen().x, dimension.height - getSize().height);
            }
            onLocationChange(getLocation());
        }
    }
}
