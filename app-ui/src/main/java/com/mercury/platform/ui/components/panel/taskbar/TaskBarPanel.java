package com.mercury.platform.ui.components.panel.taskbar;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TaskBarPanel extends JPanel implements ViewInit {
    private ComponentsFactory componentsFactory;
    private TaskBarController controller;
    public TaskBarPanel(@NonNull TaskBarController controller){
        this.controller = controller;
        this.componentsFactory = new ComponentsFactory();
        onViewInit();
    }
    public TaskBarPanel(@NonNull TaskBarController controller, @NonNull ComponentsFactory factory){
        this.controller = controller;
        this.componentsFactory = factory;
        onViewInit();
    }

    @Override
    public void onViewInit() {
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));

        JButton visibleMode = componentsFactory.getIconButton(
                "app/visible-always-mode.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                TooltipConstants.VISIBLE_MODE);
        componentsFactory.setUpToggleCallbacks(visibleMode,
                () -> {
                    visibleMode.setIcon(componentsFactory.getIcon("app/visible-dnd-mode.png", 24));
                    controller.enableDND();
                },
                () -> {
                    visibleMode.setIcon(componentsFactory.getIcon("app/visible-always-mode.png", 24));
                    controller.disableDND();
                },
                true
        );

        JButton itemGrid = componentsFactory.getIconButton(
                "app/item-grid-enable.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                TooltipConstants.ITEM_GRID);
        itemGrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    controller.showITH();
                }
            }
        });

        JButton toHideOut = componentsFactory.getIconButton(
                "app/hideout.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                TooltipConstants.HIDEOUT);
        toHideOut.addActionListener(action -> {
            this.controller.performHideout();
            this.repaint();
        });
        JButton adr = componentsFactory.getIconButton(
                "app/overseer_icon.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                TooltipConstants.ADR_SETTINGS);
        adr.addActionListener(action -> {
            FramesManager.INSTANCE.performAdr();
            this.repaint();
        });

        JButton chatFilter = componentsFactory.getIconButton(
                "app/chat-filter.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                TooltipConstants.CHAT_FILTER);
        chatFilter.addActionListener(action -> {
            this.controller.showChatFiler();
            this.repaint();
        });

        JButton historyButton = componentsFactory.getIconButton(
                "app/history.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                TooltipConstants.HISTORY);
        historyButton.addActionListener(action -> {
            this.controller.showHistory();
            this.repaint();
        });

        JButton pinButton = componentsFactory.getIconButton(
                "app/drag_and_drop.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                TooltipConstants.SETUP_FRAMES_LOCATION);
        pinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    controller.openPINSettings();
                }
            }
        });

        JButton scaleButton = componentsFactory.getIconButton(
                "app/scale-settings.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                TooltipConstants.SCALE_SETTINGS);
        scaleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    controller.openScaleSettings();
                }
            }
        });

        JButton settingsButton = componentsFactory.getIconButton(
                "app/settings.png",
                26,
                AppThemeColor.FRAME_ALPHA,
                "");
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    controller.showSettings();
                }
            }
        });

        JButton exitButton = componentsFactory.getIconButton(
                "app/exit.png",
                24,
                AppThemeColor.FRAME_ALPHA,
                "");
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    controller.exit();
                }
            }
        });

        this.add(toHideOut);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(adr);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(chatFilter);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(visibleMode);
        this.add(Box.createRigidArea(new Dimension(2, 4)));
        this.add(historyButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(itemGrid);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(pinButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(scaleButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(settingsButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(exitButton);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
    }

    public int getWidthOf(int elementCount){
        int size = this.getPreferredSize().width / (this.getComponentCount()/2);
        return size * elementCount + 3;
    }
}
