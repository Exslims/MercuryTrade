package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.CloseGridItemEvent;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;

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

    public ItemInfoPanel(ItemMessage message) {
        this.message = message;
        componentsFactory = new ComponentsFactory();
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
            EventRouter.INSTANCE.fireEvent(new CloseGridItemEvent(message));
        });
        this.add(hideButton,BorderLayout.LINE_END);

        JPanel tabInfoPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        tabInfoPanel.add(componentsFactory.getTextLabel("Tab: " +message.getTabName()));
        tabInfoPanel.setBorder(BorderFactory.createEmptyBorder(-8,0,-6,0));
        this.add(tabInfoPanel,BorderLayout.PAGE_END);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_DEFAULT, 1));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!getBounds().contains(e.getPoint())) {
                    setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
                }
            }
        });
    }
}
