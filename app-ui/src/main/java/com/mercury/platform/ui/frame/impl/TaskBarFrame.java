package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.ui.components.test.TestCasesFrame;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Exslims
 * 07.12.2016
 */
public class TaskBarFrame extends OverlaidFrame {
    private final int MINIMUM_WIDTH = 114;
    private MessageFrame messageFrame;
    private TestCasesFrame testCasesFrame;
    private HistoryFrame historyFrame;
    private NotificationFrame notificationFrame;

    private Timeline collapseAnim;

    public TaskBarFrame() {
        super("MercuryTrader");
    }

    @Override
    protected void init() {
        messageFrame = new MessageFrame();
        testCasesFrame = new TestCasesFrame();
        historyFrame = new HistoryFrame();
        notificationFrame = new NotificationFrame();
        super.init();
        add(getTaskBarPanel());
        pack();
        this.setSize(new Dimension(MINIMUM_WIDTH,this.getHeight()));
        disableHideEffect();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(collapseAnim != null){
                    collapseAnim.abort();
                }
                initCollapseAnimations("expand");
                collapseAnim.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!isMouseWithInFrame()) {
                    if (collapseAnim != null) {
                        collapseAnim.abort();
                    }
                    initCollapseAnimations("collapse");
                    collapseAnim.play();
                }
            }
        });
        EventRouter.fireEvent(new UILoadedEvent());
    }

    @Override
    protected String getFrameTitle() {
        return null;
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new FlowLayout(FlowLayout.LEFT);
    }

    private JPanel getTaskBarPanel(){
        JPanel taskBarPanel = new JPanel();
        taskBarPanel.setBackground(AppThemeColor.TRANSPARENT);
        taskBarPanel.setLayout(new BoxLayout(taskBarPanel,BoxLayout.X_AXIS));

        JButton visibleMode = componentsFactory.getIconButton("app/visible-always-mode.png",24);
        visibleMode.addMouseListener(new MouseAdapter() {
            private String currentMode = "always";
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentMode.equals("always")) {
                    visibleMode.setIcon(componentsFactory.getIcon("app/visible-dnd-mode.png", 24));
                    currentMode = "dnd";
                    TaskBarFrame.this.repaint();
                    EventRouter.fireEvent(new NotificationEvent("DND on"));
                } else {
                    currentMode = "always";
                    visibleMode.setIcon(componentsFactory.getIcon("app/visible-always-mode.png", 24));
                    TaskBarFrame.this.repaint();
                    EventRouter.fireEvent(new NotificationEvent("DND off"));
                }
            }
        });

        JButton chatMode = componentsFactory.getIconButton("app/standard-mode.png",24);
        chatMode.addMouseListener(new MouseAdapter() {
            private String currentMode = "standard";
            @Override
            public void mouseClicked(MouseEvent e) {
                if(currentMode.equals("standard")){
                    chatMode.setIcon(componentsFactory.getIcon("app/supertrade-mode.png",24));
                    currentMode = "supertrade";
                    TaskBarFrame.this.repaint();
                    EventRouter.fireEvent(new NotificationEvent("SuperTrade mode ON"));
                    EventRouter.fireEvent(new ChangedTradeModeEvent.ToSuperTradeModeEvent());
                }else {
                    currentMode = "standard";
                    chatMode.setIcon(componentsFactory.getIcon("app/standard-mode.png",24));
                    TaskBarFrame.this.repaint();
                    EventRouter.fireEvent(new NotificationEvent("SuperTrade mode OFF"));
                    EventRouter.fireEvent(new ChangedTradeModeEvent.ToDefaultTradeModeEvent());
                }
            }
        });

        JButton historyButton = componentsFactory.getIconButton("app/history.png",24);
        historyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!historyFrame.isVisible()) {
                    historyFrame.setVisible(true);
                }else {
                    historyFrame.setVisible(false);
                }
            }
        });

        JButton moveButton = componentsFactory.getIconButton("app/drag_and_drop.png", 24);
        moveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        JButton settingsButton = componentsFactory.getIconButton("app/settings.png", 26);
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    SwingUtilities.invokeLater(()->{
                        SettingsFrame settingsFrame = new SettingsFrame();
                        settingsFrame.setVisible(true);
                    });
                }
            }
        });
        settingsButton.setToolTipText("Settings");

        JButton exitButton = componentsFactory.getIconButton("app/exit.png", 24);
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messageFrame.dispose();
                testCasesFrame.dispose();
                System.exit(0);
            }
        });

        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));
        taskBarPanel.add(visibleMode);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));
        taskBarPanel.add(chatMode);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));
        taskBarPanel.add(historyButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));
        taskBarPanel.add(moveButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));
        taskBarPanel.add(settingsButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));
        taskBarPanel.add(exitButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));

        // adding drag frame listeners
        visibleMode.addMouseListener(new DraggedFrameMouseListener());
        visibleMode.addMouseMotionListener(new DraggedFrameMotionListener());
        chatMode.addMouseListener(new DraggedFrameMouseListener());
        chatMode.addMouseMotionListener(new DraggedFrameMotionListener());
        historyButton.addMouseListener(new DraggedFrameMouseListener());
        historyButton.addMouseMotionListener(new DraggedFrameMotionListener());
        moveButton.addMouseListener(new DraggedFrameMouseListener());
        moveButton.addMouseMotionListener(new DraggedFrameMotionListener());
        settingsButton.addMouseListener(new DraggedFrameMouseListener());
        settingsButton.addMouseMotionListener(new DraggedFrameMotionListener());
        return taskBarPanel;
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(RepaintEvent.RepaintTaskBar.class, event -> {
            TaskBarFrame.this.revalidate();
            TaskBarFrame.this.repaint();
        });
    }
    private void initCollapseAnimations(String state){
        collapseAnim = new Timeline(this);
        switch (state){
            case "expand":{
                collapseAnim.addPropertyToInterpolate("width",this.getWidth(),this.getPreferredSize().width);
                break;
            }
            case "collapse":{
                collapseAnim.addPropertyToInterpolate("width",this.getWidth(),MINIMUM_WIDTH);
            }
        }
        collapseAnim.setEase(new Spline(1f));
        collapseAnim.setDuration(300);
    }

    /**
     * For 'trident' property animations
     * @param width next width
     */
    public void setWidth(int width){
        this.setSize(new Dimension(width,this.getHeight()));
    }
}
