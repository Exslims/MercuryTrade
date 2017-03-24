package com.mercury.platform.ui.frame.movable;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.pojo.StashTab;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.HorizontalScrollContainer;
import com.mercury.platform.ui.components.panel.grid.*;
import com.mercury.platform.ui.misc.event.*;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ItemsGridFrame extends MovableComponentFrame{
    private ItemsGridPanel itemsGridPanel;
    private HorizontalScrollContainer tabsContainer;
    private StashTabsContainer stashTabsContainer;
    public ItemsGridFrame() {
        super("MercuryTrade");
        enableMouseOverBorder = false;
        processHideEffect = false;
        itemsGridPanel = new ItemsGridPanel();
        stashTabsContainer = new StashTabsContainer();
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.getRootPane().setBorder(null);
        this.add(itemsGridPanel,BorderLayout.CENTER);
        this.setPreferredSize(this.getMaximumSize());
        this.pack();
    }

    @Override
    public void initHandlers() {
        EventRouter.UI.registerHandler(ShowItemGridEvent.class, event -> {
            if(configManager.isItemsGridEnable()) {
                ItemMessage message = ((ShowItemGridEvent) event).getMessage();
                if (itemsGridPanel.getActiveTabsCount() == 0) {
                    this.setVisible(true);
                }
                itemsGridPanel.add(message,null);
                this.pack();
            }
        });
        EventRouter.UI.registerHandler(CloseMessagePanelEvent.class, event -> {
            Message message = ((CloseMessagePanelEvent) event).getMessage();
            if(message instanceof ItemMessage) {
                itemsGridPanel.remove((ItemMessage) message);
                if (itemsGridPanel.getActiveTabsCount() == 0) {
                    this.setVisible(false);
                }
            }
        });
        EventRouter.UI.registerHandler(CloseGridItemEvent.class, event -> {
            itemsGridPanel.remove(((CloseGridItemEvent) event).getMessage());
        });
        EventRouter.UI.registerHandler(RepaintEvent.RepaintItemGrid.class, event -> {
            this.revalidate();
            this.repaint();
        });
        EventRouter.UI.registerHandler(DismissStashTabInfoEvent.class,event -> {
            TabInfoPanel tabInfoPanel = ((DismissStashTabInfoEvent) event).getTabInfoPanel();
            tabsContainer.remove(tabInfoPanel);
            stashTabsContainer.removeTab(tabInfoPanel.getStashTab());
            this.repaint();
            this.pack();
        });
        EventRouter.UI.registerHandler(ItemCellStateChangedEvent.class, event -> {
            ItemInfoPanel itemInfoPanel = ((ItemCellStateChangedEvent) event).getItemInfoPanel();
            itemsGridPanel.changeTabType(itemInfoPanel);
            this.pack();
        });
    }
    @Override
    protected void onLock() {
        super.onLock();
        if(itemsGridPanel.getActiveTabsCount() > 0){
            this.setVisible(true);
        }
    }

    @Override
    protected JPanel getPanelForPINSettings() {
        JPanel panel = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel topPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        topPanel.setBackground(AppThemeColor.FRAME);
        JPanel headerPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel defaultGridPanel = componentsFactory.getTransparentPanel(new GridLayout(12,12));
        defaultGridPanel.setBorder(null);
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 12; y++) {
                defaultGridPanel.add(getCellPlaceholder());
            }
        }
        defaultGridPanel.setBackground(AppThemeColor.FRAME_ALPHA);

        JPanel quadGridPanel = componentsFactory.getTransparentPanel(new GridLayout(24,24));
        quadGridPanel.setBorder(null);
        for (int x = 0; x < 24; x++) {
            for (int y = 0; y < 24; y++) {
                quadGridPanel.add(getCellPlaceholder());
            }
        }
        quadGridPanel.setBackground(AppThemeColor.FRAME_ALPHA);
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox tabType = componentsFactory.getComboBox(new String[]{"1x1", "4x4"});
        tabType.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                String item = (String)e.getItem();
                if(item.equals("4x4")){
                    panel.remove(defaultGridPanel);
                    panel.add(quadGridPanel, BorderLayout.CENTER);
                    this.pack();
                    this.repaint();
                }else {
                    panel.remove(quadGridPanel);
                    panel.add(defaultGridPanel, BorderLayout.CENTER);
                    this.pack();
                    this.repaint();
                }
            }
        });

        labelPanel.add(tabType);
        Color titleColor = configManager.isItemsGridEnable()?AppThemeColor.TEXT_NICKNAME:AppThemeColor.TEXT_DISABLE;
        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.BOLD, titleColor, TextAlignment.LEFTOP, 20f, "Align this grid(approximately)");
        labelPanel.add(titleLabel);
        headerPanel.add(labelPanel, BorderLayout.CENTER);

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
        JButton hideButton  = componentsFactory.getBorderedButton("Save");
        hideButton.setPreferredSize(new Dimension(90,24));
        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    stashTabsContainer.save();
                    FramesManager.INSTANCE.disableMovement(ItemsGridFrame.class);
                }
            }
        });
        JPanel disablePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.RIGHT));
        disablePanel.add(disableButton);
        disablePanel.add(hideButton);

        JPanel savedTabsPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        savedTabsPanel.setPreferredSize(new Dimension(50,56));
        tabsContainer = new HorizontalScrollContainer();
        tabsContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        tabsContainer.setBackground(AppThemeColor.TRANSPARENT);
        JScrollPane scrollPane = new JScrollPane(tabsContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppThemeColor.FRAME);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                repaint();
            }
        });
        JScrollBar hBar = scrollPane.getHorizontalScrollBar();
        hBar.setBackground(AppThemeColor.SLIDE_BG);
        hBar.setUI(new MercuryScrollBarUI());
        hBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 16));
        hBar.setUnitIncrement(3);
        hBar.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        hBar.addAdjustmentListener(e -> repaint());

        savedTabsPanel.add(scrollPane,BorderLayout.CENTER);
        tabsContainer.getParent().setBackground(AppThemeColor.TRANSPARENT);
        stashTabsContainer.getStashTabs().forEach(stashTab -> {
            TabInfoPanel tabInfoPanel = new TabInfoPanel(stashTab);
            tabsContainer.add(tabInfoPanel);
        });

        headerPanel.add(disablePanel,BorderLayout.LINE_END);
        topPanel.add(headerPanel,BorderLayout.PAGE_START);
        topPanel.add(savedTabsPanel,BorderLayout.CENTER);
        panel.add(topPanel,BorderLayout.PAGE_START);
        panel.add(defaultGridPanel,BorderLayout.CENTER);
        setUpResizePanels(panel);
        return panel;
    }

    private JPanel getCellPlaceholder(){
        JPanel cell = new JPanel();
        cell.setOpaque(true);
        cell.setBackground(AppThemeColor.TRANSPARENT);
        cell.setBorder(BorderFactory.createLineBorder(AppThemeColor.SCROLL_BAR));
        return cell;
    }

    private void setUpResizePanels(JPanel root){
        JLabel rightArrow = componentsFactory.getIconLabel("app/default-mp.png",16); //todo
        JPanel rightPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rightPanel.setBackground(AppThemeColor.FRAME);
        rightPanel.add(rightArrow,BorderLayout.CENTER);

        rightPanel.addMouseListener(new ArrowMouseListener(rightPanel,new Cursor(Cursor.E_RESIZE_CURSOR)));
        rightPanel.addMouseMotionListener(new ResizeByWidthMouseMotionListener());
        JLabel downArrow = componentsFactory.getIconLabel("app/expand-mp.png",16); //todo
        JPanel downPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.setBorder(BorderFactory.createEmptyBorder(-10,0,0,0));
        downPanel.setBackground(AppThemeColor.FRAME);
        downPanel.add(downArrow);

        downPanel.addMouseListener(new ArrowMouseListener(downPanel, new Cursor(Cursor.N_RESIZE_CURSOR)));
        downPanel.addMouseMotionListener(new ResizeByHeightMouseMotionListener());

        root.add(rightPanel,BorderLayout.LINE_END);
        root.add(downPanel,BorderLayout.PAGE_END);
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    @Override
    protected JPanel defaultView(ComponentsFactory factory) {
        ItemsGridPanel defaultView = new ItemsGridPanel(factory);
        ItemMessage message = new ItemMessage();
        message.setWhisperNickname("Example1");
        message.setTabName("Example");
        message.setLeft(5);
        message.setTop(5);

        ItemInfoPanelController controller = new ItemInfoPanelController() {
            @Override
            public void hidePanel() {}
            @Override
            public void changeTabType(@NonNull ItemInfoPanel panel) {}
        };
        defaultView.add(message,controller);
        return defaultView;
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
    private class ArrowMouseListener extends MouseAdapter {
        private JPanel panel;
        private Cursor cursor;

        private ArrowMouseListener(JPanel panel, Cursor cursor) {
            this.panel = panel;
            this.cursor = cursor;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            panel.setBackground(AppThemeColor.TEXT_DISABLE);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            panel.setBackground(AppThemeColor.FRAME);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(cursor);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
