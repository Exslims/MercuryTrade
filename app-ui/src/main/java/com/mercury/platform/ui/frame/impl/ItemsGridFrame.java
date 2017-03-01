package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.CloseMessagePanelEvent;
import com.mercury.platform.shared.events.custom.ShowItemMeshEvent;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.grid.ItemCell;
import com.mercury.platform.ui.components.panel.grid.ItemInfoPanel;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 18.02.2017.
 */
public class ItemsGridFrame extends MovableComponentFrame{
    private List<ItemCell> cells;
    private Map<String,ItemInfoPanel> tabButtons;
    private JPanel navBar;
    private JLabel prevLabel;

    public ItemsGridFrame() {
        super("MT-Mesh");
        cells = new ArrayList<>();
        tabButtons = new HashMap<>();
        enableMouseOverBorder = false;
        processHideEffect = false;
    }

    @Override
    protected void initialize() {
        super.initialize();
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
                prevLabel = label;
                itemsMesh.add(label,column);
            }
        }
        JPanel rightPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rightPanel.setBackground(AppThemeColor.TRANSPARENT);
        rightPanel.setPreferredSize(new Dimension(16,668));
        JPanel downPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.setBorder(BorderFactory.createEmptyBorder(-10,0,0,0));
        downPanel.setBackground(AppThemeColor.TRANSPARENT);
        downPanel.setPreferredSize(new Dimension(661,16));
        downPanel.addMouseMotionListener(new ResizeByHeightMouseMotionListener());

        this.add(getHeaderPanel(),BorderLayout.PAGE_START);
        this.add(itemsMesh, BorderLayout.CENTER);
        this.add(rightPanel,BorderLayout.LINE_END);
        this.add(downPanel,BorderLayout.PAGE_END);
        this.setPreferredSize(this.getMaximumSize());
        this.pack();
    }

    private JPanel getHeaderPanel(){
        JPanel root = componentsFactory.getTransparentPanel();
        root.setLayout(new BoxLayout(root,BoxLayout.Y_AXIS));

        navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel emptyPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        emptyPanel.setPreferredSize(new Dimension(50,24));

        root.add(navBar);
        root.add(emptyPanel);
        return root;
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(ShowItemMeshEvent.class, event -> {
            if(configManager.isItemsGridEnable()) {
                String nickname = ((ShowItemMeshEvent) event).getNickname();
                String tabInfo = ((ShowItemMeshEvent) event).getTabInfo();
                //now support only poe trade
                if (!tabInfo.contains("[") && !tabButtons.containsKey(nickname + tabInfo)) {
                    String tab = StringUtils.substringBetween(tabInfo, "\"", "\"");
                    int x = Integer.parseInt(StringUtils.substringBetween(tabInfo, "left ", ","));
                    int y = Integer.parseInt(StringUtils.substringAfter(tabInfo, "top "));
                    ItemInfoPanel button = new ItemInfoPanel(nickname, tab);
                    button.setAlignmentY(SwingConstants.CENTER);
                    button.addMouseListener(new MouseAdapter() {
                        private Timer timer;

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            Optional<ItemCell> targetCell = cells
                                    .stream()
                                    .filter(cell -> (cell.getX() == x && cell.getY() == y))
                                    .findFirst();
                            targetCell.ifPresent(itemCell -> {
                                if (timer != null && timer.isRunning()) {
                                    timer.stop();
                                }
                                itemCell.getLabel().setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_DEFAULT, 2));
                            });
                            repaint();
                            pack();
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            Optional<ItemCell> targetCell = cells
                                    .stream()
                                    .filter(cell -> (cell.getX() == x && cell.getY() == y))
                                    .findFirst();
                            targetCell.ifPresent(itemCell -> {
                                timer = new Timer(1500, null);
                                timer.addActionListener(action -> {
                                    timer.stop();
                                    itemCell.getLabel().setBorder(null);
                                    repaint();
                                });
                                timer.start();
                            });
                            repaint();
                            pack();
                        }
                    });
                    if (navBar.getComponentCount() == 0) {
                        this.setVisible(true);
                    }
                    navBar.add(button);
                    tabButtons.put(nickname + tabInfo, button);
                    pack();
                }
            }
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
            Message message = ((CloseMessagePanelEvent) event).getMessage();
            if(message instanceof ItemMessage) {
                String tabInfo = ((ItemMessage) message).getTabInfo();
                String nickname = message.getWhisperNickname();
                ItemInfoPanel tabButton = tabButtons.get(nickname + tabInfo);
                if(tabButton != null){
                    navBar.remove(tabButton);
                    int x = Integer.parseInt(StringUtils.substringBetween(tabInfo,"left ",","));
                    int y = Integer.parseInt(StringUtils.substringAfter(tabInfo,"top "));
                    Optional<ItemCell> targetCell = cells
                            .stream()
                            .filter(cell -> (cell.getX() == x && cell.getY() == y))
                            .findFirst();
                    targetCell.ifPresent(itemCell -> {
                        itemCell.getLabel().setBorder(null);
                        repaint();
                    });
                    tabButtons.remove(nickname+tabInfo);
                    this.pack();
                    this.repaint();
                    if(navBar.getComponentCount() == 0){
                        this.setVisible(false);
                    }
                }
            }
        });

    }

    @Override
    protected JPanel panelWhenMove() {
        JPanel panel = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel topPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        topPanel.setBackground(AppThemeColor.FRAME);
        topPanel.setPreferredSize(new Dimension(50,68));
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        Color titleColor = configManager.isItemsGridEnable()?AppThemeColor.TEXT_NICKNAME:AppThemeColor.TEXT_DISABLE;
        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.BOLD, titleColor, TextAlignment.LEFTOP, 20f, "Align this grid with your stash");
        labelPanel.add(titleLabel);
        topPanel.add(labelPanel, BorderLayout.CENTER);
        String title = (configManager.isItemsGridEnable())?"Disable" : "Enable";
        JButton disableButton = componentsFactory.getBorderedButton(title);
        disableButton.setPreferredSize(new Dimension(90,24));
        componentsFactory.setUpToggleCallbacks(disableButton,
                () -> {
                    disableButton.setText("Enable");
                    titleLabel.setForeground(AppThemeColor.TEXT_DISABLE);
                    configManager.setItemsGridEnable(false);
                    repaint();
                },
                () -> {
                    disableButton.setText("Disable");
                    titleLabel.setForeground(AppThemeColor.TEXT_NICKNAME);
                    configManager.setItemsGridEnable(true);
                    repaint();
                },configManager.isItemsGridEnable());
        JPanel disablePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        disablePanel.add(disableButton);
        topPanel.add(disablePanel,BorderLayout.LINE_END);
        panel.add(topPanel,BorderLayout.PAGE_START);

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
                label.setBorder(BorderFactory.createLineBorder(AppThemeColor.SCROLL_BAR));
                itemsMesh.add(label,column);
            }
        }
        itemsMesh.setBackground(AppThemeColor.FRAME_ALPHA);
        panel.add(itemsMesh,BorderLayout.CENTER);
        setUpResizePanels(panel);
//        panel.setSize(this.getSize());
        return panel;
    }

    private void setUpResizePanels(JPanel root){
        JLabel rightArrow = componentsFactory.getIconLabel("app/default-mp.png",16); //todo
        JPanel rightPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rightPanel.setBackground(AppThemeColor.FRAME);
        rightPanel.add(rightArrow,BorderLayout.CENTER);

        rightPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                rightPanel.setBackground(AppThemeColor.TEXT_DISABLE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rightPanel.setBackground(AppThemeColor.FRAME);
            }
        });
        rightPanel.addMouseMotionListener(new ResizeByWidthMouseMotionListener());

        JLabel downArrow = componentsFactory.getIconLabel("app/expand-mp.png",16); //todo
        JPanel downPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.setBorder(BorderFactory.createEmptyBorder(-10,0,0,0));
        downPanel.setBackground(AppThemeColor.FRAME);
        downPanel.add(downArrow);

        downPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                downPanel.setBackground(AppThemeColor.TEXT_DISABLE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                downPanel.setBackground(AppThemeColor.FRAME);
            }
        });
        downPanel.addMouseMotionListener(new ResizeByHeightMouseMotionListener());

        root.add(rightPanel,BorderLayout.LINE_END);
        root.add(downPanel,BorderLayout.PAGE_END);
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    private class ResizeByWidthMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            sizeWasChanged = true;
            JPanel source = (JPanel) e.getSource();
            Point frameLocation = getLocation();
            setSize(new Dimension(e.getLocationOnScreen().x - frameLocation.x + source.getWidth(), getHeight()));
            configManager.saveFrameSize(ItemsGridFrame.class.getSimpleName(),getSize());
        }
    }
    private class ResizeByHeightMouseMotionListener extends MouseMotionAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            sizeWasChanged = true;
            JPanel source = (JPanel) e.getSource();
            Point frameLocation = getLocation();
            setSize(new Dimension(getWidth(),e.getLocationOnScreen().y - frameLocation.y + source.getHeight()));
            configManager.saveFrameSize(ItemsGridFrame.class.getSimpleName(),getSize());
        }
    }
}
