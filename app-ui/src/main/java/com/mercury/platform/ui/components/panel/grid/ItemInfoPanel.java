package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.ui.misc.event.CloseGridItemEvent;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.event.RepaintEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 01.03.2017.
 */
public class ItemInfoPanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    private ItemMessage message;
    private JPanel cell;

    public ItemInfoPanel(ItemMessage message, JPanel cell) {
        this.message = message;
        this.cell = cell;
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
        JCheckBox isItQuad = componentsFactory.getCheckBox("todo");
        tabInfoPanel.add(isItQuad);
        this.add(tabInfoPanel,BorderLayout.PAGE_END);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
    }
    private void setupMouseOverListener(){
        this.addMouseListener(new MouseAdapter() {
            private Timer timer;
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
}
