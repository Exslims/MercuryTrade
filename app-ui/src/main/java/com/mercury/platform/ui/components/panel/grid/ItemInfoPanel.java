package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.pojo.StashTab;
import com.mercury.platform.ui.misc.TooltipConstants;
import com.mercury.platform.ui.misc.event.CloseGridItemEvent;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.event.ItemCellStateChangedEvent;
import com.mercury.platform.ui.misc.event.RepaintEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemInfoPanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    private ItemMessage message;
    private JPanel cell;
    private StashTab stashTab;
    private ItemCell itemCell;
    private Timer timer;

    public ItemInfoPanel(ItemMessage message, ItemCell itemCell, StashTab stashTab) {
        this.message = message;
        this.cell = itemCell.getCell();
        this.itemCell = itemCell;
        this.stashTab = stashTab;
        componentsFactory = new ComponentsFactory();
        setupMouseOverListener();
        createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);

        JLabel nicknameLabel = componentsFactory.getTextLabel(message.getWhisperNickname());
        JPanel nicknamePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        nicknameLabel.setForeground(AppThemeColor.TEXT_NICKNAME);
        nicknamePanel.add(nicknameLabel);
        nicknamePanel.setBorder(BorderFactory.createEmptyBorder(-6,0,-6,0));
        this.add(nicknamePanel,BorderLayout.CENTER);

        JButton hideButton = componentsFactory.getIconButton("app/close.png", 10, AppThemeColor.FRAME_ALPHA, "Close");
        hideButton.addActionListener((action)->{
            EventRouter.UI.fireEvent(new CloseGridItemEvent(message));
        });
        this.add(hideButton,BorderLayout.LINE_END);

        JPanel tabInfoPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tabLabel = componentsFactory.getTextLabel("Tab: " + message.getTabName());
        tabInfoPanel.add(tabLabel);
        tabInfoPanel.setBorder(BorderFactory.createEmptyBorder(-8,0,-6,0));
        if(stashTab.isUndefined()) {
            JCheckBox isItQuad = componentsFactory.getCheckBox(TooltipConstants.IS_IT_QUAD);
            tabInfoPanel.add(isItQuad);

            isItQuad.addActionListener(action->{
                stashTab.setQuad(isItQuad.isSelected());
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                cell.setBorder(null);
                EventRouter.UI.fireEvent(new ItemCellStateChangedEvent(ItemInfoPanel.this));
            });
        }

        this.add(tabInfoPanel,BorderLayout.PAGE_END);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
    }
    private void setupMouseOverListener(){
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
                if(!getBounds().contains(e.getPoint())) {
                    setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                }
                timer = new Timer(1500, null);
                timer.addActionListener(action -> {
                    timer.stop();
                    cell.setBorder(null);
                    EventRouter.UI.fireEvent(new RepaintEvent.RepaintItemGrid());
                });
                timer.start();
            }
        });
    }

    public StashTab getStashTab() {
        return stashTab;
    }

    public void setStashTab(StashTab stashTab) {
        this.stashTab = stashTab;
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
