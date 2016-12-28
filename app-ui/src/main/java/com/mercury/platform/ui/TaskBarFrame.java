package com.mercury.platform.ui;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.SCEventHandler;
import com.mercury.platform.shared.events.custom.ChangeFrameVisibleEvent;
import com.mercury.platform.shared.events.custom.NewPatchSCEvent;
import com.mercury.platform.shared.events.custom.NotificationEvent;
import com.mercury.platform.shared.events.custom.RepaintEvent;
import com.mercury.platform.ui.components.test.TestCasesFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Exslims
 * 07.12.2016
 */
public class TaskBarFrame extends OverlaidFrame {
    private MessageFrame messageFrame;
    private TestCasesFrame testCasesFrame;
    private HistoryFrame historyFrame;
    private NotificationFrame notificationFrame;

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
        disableHideEffect();
        pack();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new FlowLayout();
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
                if(currentMode.equals("always")){
                    visibleMode.setIcon(componentsFactory.getIcon("app/visible-dnd-mode.png",24));
                    currentMode = "dnd";
                    TaskBarFrame.this.repaint();
                    EventRouter.fireEvent(new NotificationEvent("DND on"));
                }else {
                    currentMode = "always";
                    visibleMode.setIcon(componentsFactory.getIcon("app/visible-always-mode.png",24));
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
                }else {
                    currentMode = "standard";
                    chatMode.setIcon(componentsFactory.getIcon("app/standard-mode.png",24));
                    TaskBarFrame.this.repaint();
                    EventRouter.fireEvent(new NotificationEvent("SuperTrade mode OFF"));
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

            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        historyButton.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                e.translatePoint(TaskBarFrame.this.getLocation().x - x,TaskBarFrame.this.getLocation().y - y);
                TaskBarFrame.this.setLocation(e.getX(),e.getY());
                configManager.saveComponentLocation(TaskBarFrame.this.getClass().getSimpleName(),TaskBarFrame.this.getLocation());
            }
        });
        JButton settingsButton = componentsFactory.getIconButton("app/settings.png", 26);
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    SettingsFrame settingsFrame = new SettingsFrame();
                    settingsFrame.setVisible(true);
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
        taskBarPanel.add(settingsButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));
        taskBarPanel.add(exitButton);
        taskBarPanel.add(Box.createRigidArea(new Dimension(2,2)));
        return taskBarPanel;
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(NewPatchSCEvent.class, event -> {
            JFrame frame = new JFrame("New patch");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JLabel label = new JLabel(((NewPatchSCEvent)event).getPatchTitle());
            frame.getContentPane().add(label);
            frame.pack();
            frame.setVisible(true);
        });

        EventRouter.registerHandler(RepaintEvent.class, event -> {
            TaskBarFrame.this.revalidate();
            TaskBarFrame.this.repaint();
        });

        EventRouter.registerHandler(ChangeFrameVisibleEvent.class, new SCEventHandler<ChangeFrameVisibleEvent>() {
            @Override
            public void handle(ChangeFrameVisibleEvent event) {
                switch (event.getStates()){
                    case SHOW:{
                        if(!TaskBarFrame.this.isShowing()) {
                            TaskBarFrame.this.setVisible(true);
                            messageFrame.setVisible(true);
                            testCasesFrame.setVisible(true);
                            historyFrame.setVisible(true);
                        }
                    }
                    break;
                    case HIDE:{
                        if(TaskBarFrame.this.isShowing()) {
                            TaskBarFrame.this.setVisible(false);
                            messageFrame.setVisible(false);
                            testCasesFrame.setVisible(false);
                            historyFrame.setVisible(false);
                        }
                    }
                    break;
                }
            }
        });
    }
}
