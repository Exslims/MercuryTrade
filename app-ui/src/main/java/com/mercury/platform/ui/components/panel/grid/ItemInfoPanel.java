package com.mercury.platform.ui.components.panel.grid;

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
    private String nickName;
    private String tabName;

    public ItemInfoPanel(String nickName, String tabName) {
        this.nickName = nickName;
        this.tabName = tabName;
        componentsFactory = new ComponentsFactory();
        createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);

        JLabel nicknameLabel = componentsFactory.getTextLabel(nickName);
        JPanel nicknamePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        nicknameLabel.setForeground(AppThemeColor.TEXT_NICKNAME);
        nicknamePanel.add(nicknameLabel);
        nicknamePanel.setBorder(BorderFactory.createEmptyBorder(-6,0,-6,0));
        this.add(nicknamePanel,BorderLayout.PAGE_START);

        JPanel tabInfoPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        tabInfoPanel.add(componentsFactory.getTextLabel("Tab: " +tabName));
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
                setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
            }
        });
    }
}
