package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import com.mercury.platform.ui.misc.event.RepaintEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TaskBarFrame extends MovableComponentFrame{
    private final Logger logger = LogManager.getLogger(TaskBarFrame.class.getSimpleName());
    private Timeline collapseAnimation;
    private static final int MAX_WIDTH = 250;
    private MouseListener collapseListener;

    public TaskBarFrame() {
        super("MercuryTrade");
        processEResize = false;
        processSEResize = false;
        prevState = FrameStates.SHOW;
    }

    @Override
    protected void initialize() {
        super.initialize();
        add(getTaskBarPanel(), BorderLayout.CENTER);
        pack();
        collapseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                TaskBarFrame.this.repaint();
                if (collapseAnimation != null) {
                    collapseAnimation.abort();
                }
                initCollapseAnimations("expand");
                collapseAnimation.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                TaskBarFrame.this.repaint();
                if(isVisible() && !withInPanel((JPanel)TaskBarFrame.this.getContentPane()) && !EResizeSpace) {
                    if (collapseAnimation != null) {
                        collapseAnimation.abort();
                    }
                    initCollapseAnimations("collapse");
                    collapseAnimation.play();
                }
            }
        };

        enableCollapseAnimation();
    }
    private void enableCollapseAnimation(){
        this.setWidth(configManager.getFrameSettings(this.getClass().getSimpleName()).getFrameSize().width);
        this.addMouseListener(collapseListener);
    }
    private void disableCollapseAnimation(){
        this.setWidth(MAX_WIDTH);
        this.removeMouseListener(collapseListener);
    }
    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    private JPanel getTaskBarPanel(){
        JPanel taskBarPanel = new JPanel();
        taskBarPanel.setBackground(AppThemeColor.TRANSPARENT);
        taskBarPanel.setLayout(new BoxLayout(taskBarPanel,BoxLayout.X_AXIS));


        JButton visibleMode = componentsFactory.getIconButton("app/visible-always-mode.png",24,AppThemeColor.FRAME_ALPHA, TooltipConstants.VISIBLE_MODE);
        componentsFactory.setUpToggleCallbacks(visibleMode,
                () -> {
                    visibleMode.setIcon(componentsFactory.getIcon("app/visible-dnd-mode.png", 24));
                    TaskBarFrame.this.repaint();
                    EventRouter.UI.fireEvent(new NotificationEvent("DND on"));
                    EventRouter.CORE.fireEvent(new DndModeEvent(true));
                },
                () -> {
                    visibleMode.setIcon(componentsFactory.getIcon("app/visible-always-mode.png", 24));
                    TaskBarFrame.this.repaint();
                    EventRouter.UI.fireEvent(new NotificationEvent("DND off"));
                    EventRouter.CORE.fireEvent(new DndModeEvent(false));
                },
                true
                );

        JButton itemGrid = componentsFactory.getIconButton("app/item-grid-enable.png",24,AppThemeColor.FRAME_ALPHA, TooltipConstants.ITEM_GRID);
        itemGrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    FramesManager.INSTANCE.enableOrDisableMovementDirect("ItemsGridFrame");
                }
            }
        });

        JButton toHideOut = componentsFactory.getIconButton("app/hideout.png",24,AppThemeColor.FRAME_ALPHA,TooltipConstants.HIDEOUT);
        toHideOut.addActionListener(action -> {
            EventRouter.CORE.fireEvent(new ChatCommandEvent("/hideout"));
        });

        JButton chatFilter = componentsFactory.getIconButton("app/chat-filter.png",24,AppThemeColor.FRAME_ALPHA,TooltipConstants.CHAT_FILTER);
        chatFilter.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    FramesManager.INSTANCE.hideOrShowFrame(ChatScannerFrame.class);
                }
            }
        });

        JButton historyButton = componentsFactory.getIconButton("app/history.png",24,AppThemeColor.FRAME_ALPHA,TooltipConstants.HISTORY);
        historyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    FramesManager.INSTANCE.hideOrShowFrame(HistoryFrame.class);
                }
            }
        });

        JButton moveButton = componentsFactory.getIconButton("app/drag_and_drop.png", 24,AppThemeColor.FRAME_ALPHA,TooltipConstants.SETUP_FRAMES_LOCATION);
        moveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    FramesManager.INSTANCE.enableMovementExclude("ItemsGridFrame");
                }
            }
        });

        JButton settingsButton = componentsFactory.getIconButton("app/settings.png", 26,AppThemeColor.FRAME_ALPHA,"");
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    FramesManager.INSTANCE.showFrame(SettingsFrame.class);
                }
            }
        });

        JButton exitButton = componentsFactory.getIconButton("app/exit.png", 24,AppThemeColor.FRAME_ALPHA,"");
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    FramesManager.INSTANCE.exit();
                }
            }
        });
        taskBarPanel.add(moveButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(3, 4)));
        taskBarPanel.add(toHideOut);
        taskBarPanel.add(Box.createRigidArea(new Dimension(3, 4)));
        taskBarPanel.add(visibleMode);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2, 4)));
//        taskBarPanel.add(chatFilter);
//        taskBarPanel.add(Box.createRigidArea(new Dimension(3, 4)));
        taskBarPanel.add(historyButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(3, 4)));
        taskBarPanel.add(itemGrid);
        taskBarPanel.add(Box.createRigidArea(new Dimension(3, 4)));
        taskBarPanel.add(settingsButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(3, 4)));
        taskBarPanel.add(exitButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(3, 4)));
        return taskBarPanel;
    }

    @Override
    public void initHandlers() {
        EventRouter.UI.registerHandler(RepaintEvent.RepaintTaskBar.class, event -> {
            TaskBarFrame.this.revalidate();
            TaskBarFrame.this.repaint();
        });
    }
    private void initCollapseAnimations(String state){
        collapseAnimation = new Timeline(this);
        switch (state){
            case "expand":{
                collapseAnimation.addPropertyToInterpolate("width",this.getWidth(),MAX_WIDTH);
                break;
            }
            case "collapse":{
                collapseAnimation.addPropertyToInterpolate("width",this.getWidth(),configManager.getFrameSettings(this.getClass().getSimpleName()).getFrameSize().width);
            }
        }
        collapseAnimation.setEase(new Spline(1f));
        collapseAnimation.setDuration(150);
    }
    private boolean withInPanel(JPanel panel){
        return new Rectangle(panel.getLocationOnScreen(),panel.getSize()).contains(MouseInfo.getPointerInfo().getLocation());
    }

    /**
     * For 'trident' property animations
     * @param width next width
     */
    public void setWidth(int width){
        this.setSize(new Dimension(width,this.getHeight()));
    }

    @Override
    protected void onLock() {
        super.onLock();
        enableCollapseAnimation();
    }

    @Override
    protected JPanel panelWhenMove() {
        disableCollapseAnimation();
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,20f,"Task Bar"));
        panel.add(labelPanel);
        panel.setPreferredSize(this.getSize());
        panel.setBackground(AppThemeColor.FRAME);
        return panel;
    }
}
