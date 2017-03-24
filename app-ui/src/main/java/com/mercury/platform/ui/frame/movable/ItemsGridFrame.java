package com.mercury.platform.ui.frame.movable;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.pojo.StashTab;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.HorizontalScrollContainer;
import com.mercury.platform.ui.components.panel.grid.TabInfoPanel;
import com.mercury.platform.ui.components.panel.misc.StashTabsContainer;
import com.mercury.platform.ui.misc.event.*;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.grid.ItemCell;
import com.mercury.platform.ui.components.panel.grid.ItemInfoPanel;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ItemsGridFrame extends MovableComponentFrame{
    private List<ItemCell> defaultCells;
    private List<ItemCell> quadCells;
    private Map<String,ItemInfoPanel> tabButtons;
    private StashTabsContainer stashTabsContainer;
    private JPanel navBar;

    private JPanel defaultGrid;
    private JPanel quadTabGrid;
    private HorizontalScrollContainer tabsContainer;

    public ItemsGridFrame() {
        super("MercuryTrade");
        defaultCells = new ArrayList<>();
        quadCells = new ArrayList<>();
        tabButtons = new HashMap<>();
        enableMouseOverBorder = false;
        processHideEffect = false;
        stashTabsContainer = new StashTabsContainer();
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.getRootPane().setBorder(null);
        defaultGrid = componentsFactory.getTransparentPanel(new GridLayout(12,12));
        defaultGrid.setBorder(null);
        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 12; x++) {
                ItemCell gridCell = getGridCell(x, y);
                defaultCells.add(gridCell);
                defaultGrid.add(gridCell.getCell());
            }
        }
        quadTabGrid = componentsFactory.getTransparentPanel(new GridLayout(24,24));
        quadTabGrid.setBorder(null);
        for (int y = 0; y < 24; y++) {
            for (int x = 0; x < 24; x++) {
                ItemCell gridCell = getGridCell(x, y);
                quadCells.add(gridCell);
                quadTabGrid.add(gridCell.getCell());
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
        this.add(defaultGrid, BorderLayout.CENTER);
        this.add(rightPanel,BorderLayout.LINE_END);
        this.add(downPanel,BorderLayout.PAGE_END);
        this.setPreferredSize(this.getMaximumSize());
        this.pack();
    }

    private ItemCell getGridCell(int x, int y){
        JPanel cellPanel = new JPanel();
        cellPanel.setOpaque(true);
        cellPanel.setBackground(AppThemeColor.TRANSPARENT);
        return new ItemCell(x+1,y+1,cellPanel);
    }

    private JPanel getHeaderPanel(){
        JPanel root = componentsFactory.getTransparentPanel();
        root.setLayout(new BoxLayout(root,BoxLayout.Y_AXIS));

        navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        navBar.setBorder(BorderFactory.createEmptyBorder(20,0,27,0));

        root.add(navBar);
        return root;
    }

    @Override
    public void initHandlers() {
        EventRouter.UI.registerHandler(ShowItemGridEvent.class, event -> {
            if(configManager.isItemsGridEnable()) {
                ItemMessage message = ((ShowItemGridEvent) event).getMessage();
                String nickname = message.getWhisperNickname();
                if (!tabButtons.containsKey(nickname + message.getTabName())) {
                    int x = message.getLeft();
                    int y = message.getTop();
                    StashTab stashTab;
                    if(stashTabsContainer.containsTab(message.getTabName())) {
                        stashTab = stashTabsContainer.getStashTab(message.getTabName());
                    }else {
                        stashTab = new StashTab(message.getTabName(),false);
                    }
                    Optional<ItemCell> cellByCoordinates = getCellByCoordinates(stashTab, x, y);
                    if(cellByCoordinates.isPresent()) {
                        ItemInfoPanel cellHeader = createGridItem(message, cellByCoordinates.get(), stashTab);
                        if (navBar.getComponentCount() == 0) {
                            this.setVisible(true);
                        }
                        navBar.add(cellHeader);
                        tabButtons.put(nickname + message.getTabName(), cellHeader);
                        repaint();
                        pack();
                    }
                }
            }
        });
        EventRouter.UI.registerHandler(CloseMessagePanelEvent.class, event -> {
            Message sourceMessage = ((CloseMessagePanelEvent) event).getMessage();
            if(sourceMessage instanceof ItemMessage) {
                closeGridItem((ItemMessage) sourceMessage);
            }
        });
        EventRouter.UI.registerHandler(CloseGridItemEvent.class, event -> {
            closeGridItem(((CloseGridItemEvent) event).getMessage());
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
            ItemCell itemCell = itemInfoPanel.getItemCell();
            int x = itemCell.getX();
            int y = itemCell.getY();
            Optional<ItemCell> cellByCoordinates = getCellByCoordinates(itemInfoPanel.getStashTab(), x, y);
            if(cellByCoordinates.isPresent()){
                itemInfoPanel.setCell(cellByCoordinates.get().getCell());
            }
            this.repaint();
            this.pack();
        });
    }
    private Optional<ItemCell> getCellByCoordinates(StashTab tab, int x, int y){
        Optional<ItemCell> targetCell;
        if(x > 12 || y > 12) {
            tab.setQuad(true);
        }
        if(tab.isQuad()){
            targetCell = quadCells
                    .stream()
                    .filter(cell -> (cell.getX() == x && cell.getY() == y))
                    .findFirst();
            return targetCell;
        }
        targetCell = defaultCells
                    .stream()
                    .filter(cell -> (cell.getX() == x && cell.getY() == y))
                    .findFirst();
        return targetCell;
    }
    private ItemInfoPanel createGridItem(ItemMessage message,ItemCell cell,StashTab stashTab){
        ItemInfoPanel itemInfoPanel = new ItemInfoPanel(message,cell,stashTab);
        itemInfoPanel.setAlignmentY(SwingConstants.CENTER);
        itemInfoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(stashTab.isQuad()){
                    add(quadTabGrid,BorderLayout.CENTER);
                }else {
                    add(defaultGrid,BorderLayout.CENTER);
                }
                repaint();
                pack();
            }
        });
        return itemInfoPanel;
    }
    private void closeGridItem(ItemMessage message) {
        String nickname = message.getWhisperNickname();
        ItemInfoPanel itemInfoPanel = tabButtons.get(nickname + message.getTabName());
        if (itemInfoPanel != null) {
            if(itemInfoPanel.getStashTab().isUndefined()){
                itemInfoPanel.getStashTab().setUndefined(false);
                stashTabsContainer.addTab(itemInfoPanel.getStashTab());
                stashTabsContainer.save();
            }

            navBar.remove(itemInfoPanel);
            int x = message.getLeft();
            int y = message.getTop();
            Optional<ItemCell> targetCell = defaultCells
                    .stream()
                    .filter(cell -> (cell.getX() == x && cell.getY() == y))
                    .findFirst();
            targetCell.ifPresent(itemCell -> {
                itemCell.getCell().setBorder(null);
                repaint();
            });
            tabButtons.remove(nickname + message.getTabName());
            this.pack();
            this.repaint();
            if (navBar.getComponentCount() == 0) {
                this.setVisible(false);
            }
        }
    }

    @Override
    protected void onLock() {
        super.onLock();
        if(navBar.getComponentCount() > 0){
            this.setVisible(true);
        }
    }

    @Override
    protected JPanel panelWhenMove() {
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
