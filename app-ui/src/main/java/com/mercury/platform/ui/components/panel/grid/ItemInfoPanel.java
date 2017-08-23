package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.config.descriptor.StashTabDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.frame.movable.ItemsGridFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemInfoPanel extends JPanel implements ViewInit {
    private ComponentsFactory componentsFactory;
    private ItemTradeNotificationDescriptor message;
    private JPanel cell;
    private StashTabDescriptor stashTabDescriptor;
    private ItemCell itemCell;
    private Timer timer;

    private ItemInfoPanelController controller;

    public ItemInfoPanel(ItemTradeNotificationDescriptor message, ItemCell itemCell, StashTabDescriptor stashTabDescriptor, ComponentsFactory factory) {
        this.componentsFactory = factory;
        this.controller = new ItemInfoPanelControllerImpl(message);
        this.message = message;
        this.cell = itemCell.getCell();
        this.itemCell = itemCell;
        this.stashTabDescriptor = stashTabDescriptor;
        setupMouseOverListener();
        onViewInit();
    }

    @Override
    public void onViewInit() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);

        JLabel nicknameLabel = componentsFactory.getTextLabel(message.getWhisperNickname());
        JPanel nicknamePanel = componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        nicknameLabel.setForeground(AppThemeColor.TEXT_NICKNAME);
        nicknamePanel.add(nicknameLabel);
        nicknamePanel.setBorder(BorderFactory.createEmptyBorder(-6, 0, -6, 0));
        this.add(nicknamePanel, BorderLayout.CENTER);

        JButton hideButton = componentsFactory.getIconButton("app/close.png", 12, AppThemeColor.FRAME, "Close");
        hideButton.addActionListener((action) -> {
            controller.hidePanel();
        });
        this.add(hideButton, BorderLayout.LINE_END);

        JPanel tabInfoPanel = componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tabLabel = componentsFactory.getTextLabel("Tab: " + message.getTabName());
        tabInfoPanel.add(tabLabel);
        tabInfoPanel.setBorder(BorderFactory.createEmptyBorder(-8, 0, -6, 0));
        if (stashTabDescriptor.isUndefined()) {
            JCheckBox isItQuad = componentsFactory.getCheckBox(TooltipConstants.IS_IT_QUAD);
            isItQuad.setPreferredSize(new Dimension(16, 16));
            tabInfoPanel.add(isItQuad);

            isItQuad.addActionListener(action -> {
                stashTabDescriptor.setQuad(isItQuad.isSelected());
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                cell.setBorder(null);
                controller.changeTabType(this);
            });
        }

        this.add(tabInfoPanel, BorderLayout.PAGE_END);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
    }

    private void setupMouseOverListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_DEFAULT, 1));
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                cell.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_DEFAULT, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!getBounds().contains(e.getPoint())) {
                    setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                }
                timer = new Timer(1500, null);
                timer.addActionListener(action -> {
                    timer.stop();
                    cell.setBorder(null);
                    MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
                });
                timer.start();
            }
        });
    }

    public ItemInfoPanelController getController() {
        return controller;
    }

    public void setController(ItemInfoPanelController controller) {
        this.controller = controller;
    }

    public StashTabDescriptor getStashTabDescriptor() {
        return stashTabDescriptor;
    }

    public void setStashTabDescriptor(StashTabDescriptor stashTabDescriptor) {
        this.stashTabDescriptor = stashTabDescriptor;
    }

    public ItemCell getItemCell() {
        return itemCell;
    }

    public void setItemCell(ItemCell itemCell) {
        this.itemCell = itemCell;
    }

    public JPanel getCell() {
        return cell;
    }

    public void setCell(JPanel cell) {
        this.cell = cell;
    }
}
