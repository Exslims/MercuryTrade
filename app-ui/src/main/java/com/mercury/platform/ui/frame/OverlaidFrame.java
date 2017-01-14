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
import com.mercury.platform.ui.misc.HideSettingsManager;
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
    private int HIDE_DELAY = 1000;
    private float minOpacity = 1f;
    private float maxOpacity = 1f;

    private LayoutManager layout;
    protected JPanel miscPanel;
    protected int x;
    protected int y;
    protected boolean withinResizeSpace = false;

    private Timeline hideAnimation;
    private Timeline showAnimation;
    private Timer hideTimer;
    private HideEffectListener hideEffectListener;
    private boolean hideAnimationEnable = false;


    protected FrameStates prevState;
    protected boolean processingHideEvent = true;

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
        setOpacity(minOpacity);
        setFocusableWindowState(false);
        setFocusable(false);
        this.layout = getFrameLayout();
        setLayout(layout);

        initHandlers();
        initAnimationTimers();
        initHeaderPanel();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                OverlaidFrame.this.repaint();
            }
        });
        this.addMouseListener(new ResizeMouseListener());
        this.addMouseMotionListener(new ResizeMouseMotionListener());
        HideSettingsManager.INSTANCE.registerFrame(this);

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
                if (processingHideEvent){
                    switch (event.getStates()) {
                        case SHOW: {
                            if (prevState == null) {
                                prevState = FrameStates.SHOW;
                            }
                            if (prevState.equals(FrameStates.SHOW)) {
                                OverlaidFrame.this.setAlwaysOnTop(true);
                                OverlaidFrame.this.setVisible(true);
                            }
                        }
                        break;
                        case HIDE: {
                            if (!OverlaidFrame.this.isShowing()) {
                                prevState = FrameStates.HIDE;
                            } else {
                                prevState = FrameStates.SHOW;
                            }
                            OverlaidFrame.this.setAlwaysOnTop(false);
                            OverlaidFrame.this.setVisible(false);
                        }
                        break;
                    }
                }
            }
        });

    }
    private void initHeaderPanel(){
        if(getFrameTitle() != null && layout instanceof BorderLayout) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(AppThemeColor.TRANSPARENT);
            headerPanel.setBorder(BorderFactory.createEmptyBorder(-6, 0, -6, 0));

            JLabel frameTitleLabel = componentsFactory.getTextLabel(getFrameTitle());
            frameTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            frameTitleLabel.addMouseListener(new DraggedFrameMouseListener());
            frameTitleLabel.addMouseMotionListener(new DraggedFrameMotionListener());

            headerPanel.add(frameTitleLabel, BorderLayout.CENTER);

            miscPanel = new JPanel();
            miscPanel.setBackground(AppThemeColor.TRANSPARENT);
            JButton hideButton = componentsFactory.getIconButton("app/close.png", 12, AppThemeColor.FRAME_1);
            hideButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    OverlaidFrame.this.setVisible(false);
                }
            });
            miscPanel.add(hideButton);
            headerPanel.add(miscPanel, BorderLayout.LINE_END);
            this.add(headerPanel, BorderLayout.PAGE_START);
        }
    }
    protected abstract String getFrameTitle();
    protected abstract LayoutManager getFrameLayout();

    public void disableHideEffect(){
        this.setOpacity(maxOpacity);
        this.hideAnimationEnable = false;
        if(hideEffectListener != null) {
            this.removeMouseListener(hideEffectListener);
        }
    }
    public void enableHideEffect(int delay, int minOpacity, int maxOpacity){
        this.HIDE_DELAY = delay*1000;
        this.minOpacity = minOpacity/100f;
        this.maxOpacity = maxOpacity/100f;
        this.setOpacity(minOpacity/100f);
        hideEffectListener = new HideEffectListener();
        initAnimationTimers();
        this.addMouseListener(hideEffectListener);
        this.hideAnimationEnable = true;
    }
    public void showComponent(){
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }
    public void hideComponent(){
        this.setVisible(false);
        this.setAlwaysOnTop(false);
    }

    protected boolean isMouseWithInFrame(){
        return this.getBounds().contains(MouseInfo.getPointerInfo().getLocation());
    }

    /**
     * Standard pack() method does not take into account the maximum size of frame.
     */
    protected void packFrame(){
        this.pack();
        FrameSettings frameSettings = configManager.getDefaultFramesSettings().get(this.getClass().getSimpleName());
        this.setSize(new Dimension(frameSettings.getFrameSize().width,this.getHeight()));
    }
    private void initAnimationTimers(){
        showAnimation = new Timeline(this);
        showAnimation.setDuration(SHOW_TIME);
        showAnimation.addPropertyToInterpolate("opacity", minOpacity, maxOpacity);

        hideAnimation = new Timeline(this);
        hideAnimation.setDuration(HIDE_TIME);
        hideAnimation.addPropertyToInterpolate("opacity",maxOpacity,minOpacity);
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
            FrameSettings frameSettings = configManager.getDefaultFramesSettings().get(OverlaidFrame.this.getClass().getSimpleName());
            OverlaidFrame.this.setMinimumSize(new Dimension(frameSettings.getFrameSize().width,0));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            withinResizeSpace = false;
            if(hideAnimationEnable && !isMouseWithInFrame()) {
                hideTimer.start();
            }
            Dimension size = OverlaidFrame.this.getSize();
            OverlaidFrame.this.setMinimumSize(new Dimension(size.width,0));
            configManager.saveFrameSize(OverlaidFrame.this.getClass().getSimpleName(),OverlaidFrame.this.getSize());
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
            OverlaidFrame.this.repaint();
            hideTimer.stop();
            if(OverlaidFrame.this.getOpacity() < maxOpacity) {
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
