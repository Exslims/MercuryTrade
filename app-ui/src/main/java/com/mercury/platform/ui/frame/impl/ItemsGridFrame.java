package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.CloseMessagePanelEvent;
import com.mercury.platform.shared.events.custom.ShowItemMeshEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.mesh.ItemCell;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;
import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Константин on 18.02.2017.
 */
public class ItemsGridFrame extends MovableComponentFrame{
    private List<ItemCell> cells;
    private Map<String,JButton> tabButtons;
    private JPanel navBar;
    private JLabel prevLabel;

    public ItemsGridFrame() {
        super("MT-Mesh");
        cells = new ArrayList<>();
        tabButtons = new HashMap<>();
    }

    @Override
    protected void initialize() {
        super.initialize();
        disableHideEffect();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.getRootPane().setBorder(null);

        JPanel itemsMesh = componentsFactory.getTransparentPanel(new GridBagLayout());
        GridBagConstraints column = new GridBagConstraints();
        column.weightx = 0.1f;
        column.weighty = 0.1f;
        column.fill = GridBagConstraints.BOTH;
        column.anchor = GridBagConstraints.NORTHWEST;
        for (int x = 0; x < 12; x++) {
            column.gridx = x;
            for (int y = 0; y < 12; y++) {
                column.gridy = y;
                JLabel label = componentsFactory.getTextLabel("");
                label.setOpaque(true);
                label.setBackground(AppThemeColor.TRANSPARENT);
                ItemCell cell = new ItemCell(x+1,y+1,label);
                cells.add(cell);
                itemsMesh.add(label,column);
            }
        }

        this.add(getHeaderPanel(),BorderLayout.PAGE_START);
        this.add(itemsMesh, BorderLayout.CENTER);
        this.pack();
    }

    private JPanel getHeaderPanel(){
        JPanel root = componentsFactory.getTransparentPanel();
        root.setLayout(new BoxLayout(root,BoxLayout.Y_AXIS));

        navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel emptyPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        emptyPanel.setPreferredSize(new Dimension(50,46));

        root.add(navBar);
        root.add(emptyPanel);
        return root;
    }

//     "Gear sell, unique"; position: left 7, top 1
//    (Shop [left:3,top:8])
    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(ShowItemMeshEvent.class, event -> {
            String nickname = ((ShowItemMeshEvent) event).getNickname();
            String tabInfo = ((ShowItemMeshEvent) event).getTabInfo();

            if(!tabInfo.contains("[") && !tabButtons.containsKey(tabInfo)){
                String tab = StringUtils.substringBetween(tabInfo, "\"", "\"");
                int x = Integer.parseInt(StringUtils.substringBetween(tabInfo,"left ",","));
                int y = Integer.parseInt(StringUtils.substringAfter(tabInfo,"top "));
                JButton button = componentsFactory.getBorderedButton(nickname + ":" + tab);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.BORDER, 1),
                        BorderFactory.createLineBorder(AppThemeColor.FRAME, 3)
                ));
                button.setBackground(AppThemeColor.FRAME);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        cells.forEach(cell -> {
                            if(cell.getX() == x && cell.getY() == y){
                                if(cell.getLabel().getBorder() != null){
                                    cell.getLabel().setBorder(null);
                                    prevLabel = null;
                                }else {
                                    if(prevLabel != null){
                                        prevLabel.setBorder(null); //todo
                                    }
                                    cell.getLabel().setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_IMPORTANT,5));
                                }
                                prevLabel= cell.getLabel();
                                repaint();
                            }
                        });
                    }
                });
                navBar.add(button);
                tabButtons.put(tabInfo,button);
                pack();
            }
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
            Message message = ((CloseMessagePanelEvent) event).getMessage();
            if(message instanceof ItemMessage) {
                String tabInfo = ((ItemMessage) message).getTabInfo();
                if(tabInfo != null && !tabInfo.contains("[")){
                    navBar.remove(tabButtons.get(tabInfo));
                    int x = Integer.parseInt(StringUtils.substringBetween(tabInfo,"left ",","));
                    int y = Integer.parseInt(StringUtils.substringAfter(tabInfo,"top "));
                    cells.forEach(cell -> {
                        if(cell.getX() == x && cell.getY() == y){
                            cell.getLabel().setBackground(AppThemeColor.TRANSPARENT);
                            repaint();
                        }
                    });
                    tabButtons.remove(tabInfo);
                    this.pack();
                    this.repaint();
                }
            }
        });

    }

    @Override
    protected JPanel panelWhenMove() {
        JPanel panel = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,20f,"Items mesh panel"));
        labelPanel.setPreferredSize(new Dimension(50,74));
        panel.add(labelPanel,BorderLayout.PAGE_START);

        JPanel itemsMesh = componentsFactory.getTransparentPanel(new GridBagLayout());
        GridBagConstraints column = new GridBagConstraints();
        column.weightx = 0.1f;
        column.weighty = 0.1f;
        column.fill = GridBagConstraints.BOTH;
        column.anchor = GridBagConstraints.NORTHWEST;
        for (int x = 0; x < 12; x++) {
            column.gridx = x;
            for (int y = 0; y < 12; y++) {
                column.gridy = y;
                JLabel label = componentsFactory.getTextLabel("");
                label.setOpaque(true);
                label.setBackground(AppThemeColor.TRANSPARENT);
                label.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
                itemsMesh.add(label,column);
            }
        }
        panel.add(itemsMesh,BorderLayout.CENTER);
        return panel;
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
