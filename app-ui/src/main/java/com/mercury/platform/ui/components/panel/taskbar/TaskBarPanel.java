package com.mercury.platform.ui.components.panel.taskbar;

import com.mercury.platform.core.ProdStarter;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.TaskBarDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.frame.movable.TaskBarFrame;
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
    private PlainConfigurationService<TaskBarDescriptor> taskBarService;
    private JButton toHideout;
    private JButton showHelpIG;

    public TaskBarPanel(@NonNull TaskBarController controller, @NonNull ComponentsFactory factory) {
        this.controller = controller;
        this.componentsFactory = factory;
        this.onViewInit();

        MercuryStoreCore.hotKeySubject.subscribe(hotkeyDescriptor -> {
            SwingUtilities.invokeLater(() -> {
                if (ProdStarter.APP_STATUS.equals(FrameVisibleState.SHOW)) {
                    if (this.taskBarService.get().getHideoutHotkey().equals(hotkeyDescriptor)) {
                        this.toHideout.doClick();
                    } else if (this.taskBarService.get().getHelpIGHotkey().equals(hotkeyDescriptor)) {
                        this.showHelpIG.doClick();
                    }
                }
            });
        });
    }

    @Override
    public void onViewInit() {
        this.taskBarService = Configuration.get().taskBarConfiguration();

        this.setBackground(AppThemeColor.FRAME);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JButton visibleMode = componentsFactory.getIconButton(
                "app/visible-always-mode.png",
                24,
                AppThemeColor.FRAME,
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
                AppThemeColor.FRAME,
                TooltipConstants.ITEM_GRID);
        itemGrid.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.showITH();
                }
            }
        });

        this.toHideout = componentsFactory.getIconButton(
                "app/hideout.png",
                24,
                AppThemeColor.FRAME,
                TooltipConstants.HIDEOUT);
        this.toHideout.addActionListener(action -> {
            this.controller.performHideout();
        });

        this.showHelpIG = componentsFactory.getIconButton(
                "app/helpIG_icon.png",
                24,
                AppThemeColor.FRAME,
                TooltipConstants.HELPIG);
        this.showHelpIG.addActionListener(action -> {
            this.controller.showHelpIG();
        });

        JButton adr = componentsFactory.getIconButton(
                "app/overseer_icon.png",
                24,
                AppThemeColor.FRAME,
                TooltipConstants.ADR_SETTINGS);
        adr.addActionListener(action -> {
            FramesManager.INSTANCE.performAdr();
            TaskBarFrame windowAncestor = (TaskBarFrame) SwingUtilities.getWindowAncestor(TaskBarPanel.this);
            windowAncestor.setSize(new Dimension(windowAncestor.getMIN_WIDTH(), windowAncestor.getHeight()));
            windowAncestor.pack();
        });

        JButton chatFilter = componentsFactory.getIconButton(
                "app/chat-filter.png",
                24,
                AppThemeColor.FRAME,
                TooltipConstants.CHAT_FILTER);
        chatFilter.addActionListener(action -> {
            this.controller.showChatFiler();
        });

        JButton historyButton = componentsFactory.getIconButton(
                "app/history.png",
                24,
                AppThemeColor.FRAME,
                TooltipConstants.HISTORY);
        historyButton.addActionListener(action -> {
            this.controller.showHistory();
        });

        JButton pinButton = componentsFactory.getIconButton(
                "app/drag_and_drop.png",
                24,
                AppThemeColor.FRAME,
                TooltipConstants.SETUP_FRAMES_LOCATION);
        pinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.openPINSettings();
                }
            }
        });

        JButton scaleButton = componentsFactory.getIconButton(
                "app/scale-settings.png",
                24,
                AppThemeColor.FRAME,
                TooltipConstants.SCALE_SETTINGS);
        scaleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.openScaleSettings();
                }
            }
        });

        JButton settingsButton = componentsFactory.getIconButton(
                "app/settings.png",
                26,
                AppThemeColor.FRAME,
                "");
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.showSettings();
                    TaskBarFrame windowAncestor = (TaskBarFrame) SwingUtilities.getWindowAncestor(TaskBarPanel.this);
                    windowAncestor.setSize(new Dimension(windowAncestor.getMIN_WIDTH(), windowAncestor.getHeight()));
                    windowAncestor.pack();
                }
            }
        });

        JButton exitButton = componentsFactory.getIconButton(
                "app/exit.png",
                24,
                AppThemeColor.FRAME,
                "");
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    controller.exit();
                }
            }
        });

        this.add(this.toHideout);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(adr);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(chatFilter);
        this.add(Box.createRigidArea(new Dimension(3, 4)));
        this.add(visibleMode);
        this.add(Box.createRigidArea(new Dimension(2, 4)));
        this.add(this.showHelpIG);
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

    public int getWidthOf(int elementCount) {
        int size = this.getPreferredSize().width / (this.getComponentCount() / 2);
        return size * elementCount + 3;
    }
}
