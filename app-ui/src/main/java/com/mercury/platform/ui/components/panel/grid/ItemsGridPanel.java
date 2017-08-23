package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.config.descriptor.StashTabDescriptor;
import com.mercury.platform.shared.entity.message.ItemTradeNotificationDescriptor;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.ViewInit;
import com.mercury.platform.ui.frame.movable.ItemsGridFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


public class ItemsGridPanel extends JPanel implements ViewInit {
    private ComponentsFactory componentsFactory;
    private List<ItemCell> defaultCells;
    private List<ItemCell> quadCells;
    private Map<String, ItemInfoPanel> tabButtons;
    private StashTabsContainer stashTabsContainer;
    private JPanel navBar;

    private JPanel defaultGrid;
    private JPanel quadTabGrid;

    public ItemsGridPanel() {
        super(new BorderLayout());
        componentsFactory = new ComponentsFactory();
        defaultCells = new ArrayList<>();
        quadCells = new ArrayList<>();
        tabButtons = new HashMap<>();
        stashTabsContainer = new StashTabsContainer();
        onViewInit();
    }

    public ItemsGridPanel(@NonNull ComponentsFactory factory) {
        super(new BorderLayout());
        this.componentsFactory = factory;
        defaultCells = new ArrayList<>();
        quadCells = new ArrayList<>();
        tabButtons = new HashMap<>();
        stashTabsContainer = new StashTabsContainer();
        onViewInit();
    }

    @Override
    public void onViewInit() {
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(null);

        defaultGrid = componentsFactory.getTransparentPanel(new GridLayout(12, 12));
        defaultGrid.setBorder(null);
        for (int y = 0; y < 12; y++) {
            for (int x = 0; x < 12; x++) {
                ItemCell gridCell = getGridCell(x, y);
                defaultCells.add(gridCell);
                defaultGrid.add(gridCell.getCell());
            }
        }
        quadTabGrid = componentsFactory.getTransparentPanel(new GridLayout(24, 24));
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
        rightPanel.setPreferredSize(new Dimension(18, 668));
        JPanel downPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        downPanel.setBorder(BorderFactory.createEmptyBorder(-10, 0, 0, 0));
        downPanel.setBackground(AppThemeColor.TRANSPARENT);
        downPanel.setPreferredSize(new Dimension(661, 16));

        this.add(getHeaderPanel(), BorderLayout.PAGE_START);
        this.add(defaultGrid, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.LINE_END);
        this.add(downPanel, BorderLayout.PAGE_END);
        this.setPreferredSize(this.getMaximumSize());
    }

    public void add(@NonNull ItemTradeNotificationDescriptor message, ItemInfoPanelController controller) {
        String nickname = message.getWhisperNickname();
        if (!tabButtons.containsKey(nickname + message.getTabName())) {
            int x = message.getLeft();
            int y = message.getTop();
            StashTabDescriptor stashTabDescriptor;
            if (stashTabsContainer.containsTab(message.getTabName())) {
                stashTabDescriptor = stashTabsContainer.getStashTab(message.getTabName());
            } else {
                stashTabDescriptor = new StashTabDescriptor(message.getTabName(), false, true);
            }
            Optional<ItemCell> cellByCoordinates = getCellByCoordinates(stashTabDescriptor, x, y);
            if (cellByCoordinates.isPresent()) {
                ItemInfoPanel cellHeader = createGridItem(message, cellByCoordinates.get(), stashTabDescriptor);
                if (controller != null) {
                    cellHeader.setController(controller);
                }
                navBar.add(cellHeader);
                tabButtons.put(nickname + message.getTabName(), cellHeader);
                MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
            }
        }
    }

    public void remove(@NonNull ItemTradeNotificationDescriptor message) {
        closeGridItem(message);
    }

    public void changeTabType(@NonNull ItemInfoPanel itemInfoPanel) {
        ItemCell itemCell = itemInfoPanel.getItemCell();
        int x = itemCell.getX();
        int y = itemCell.getY();
        Optional<ItemCell> cellByCoordinates = getCellByCoordinates(itemInfoPanel.getStashTabDescriptor(), x, y);
        cellByCoordinates.ifPresent(itemCell1 -> itemInfoPanel.setCell(itemCell1.getCell()));
        MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
    }

    public int getActiveTabsCount() {
        return navBar.getComponentCount();
    }

    private ItemCell getGridCell(int x, int y) {
        JPanel cellPanel = new JPanel();
        cellPanel.setOpaque(true);
        cellPanel.setBackground(AppThemeColor.TRANSPARENT);
        return new ItemCell(x + 1, y + 1, cellPanel);
    }

    private JPanel getHeaderPanel() {
        JPanel root = componentsFactory.getTransparentPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        navBar.setBorder(BorderFactory.createEmptyBorder(20, 0, 26, 0));
        root.add(navBar);
        return root;
    }

    private Optional<ItemCell> getCellByCoordinates(@NonNull StashTabDescriptor tab, int x, int y) {
        Optional<ItemCell> targetCell;
        if (x > 12 || y > 12) {
            tab.setQuad(true);
        }
        if (tab.isQuad()) {
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

    private ItemInfoPanel createGridItem(@NonNull ItemTradeNotificationDescriptor message, @NonNull ItemCell cell, @NonNull StashTabDescriptor stashTabDescriptor) {
        ItemInfoPanel itemInfoPanel = new ItemInfoPanel(message, cell, stashTabDescriptor, componentsFactory);
        itemInfoPanel.setAlignmentY(SwingConstants.CENTER);
        itemInfoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (stashTabDescriptor.isQuad()) {
                    remove(((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER));
                    add(quadTabGrid, BorderLayout.CENTER);
                } else {
                    remove(((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.CENTER));
                    add(defaultGrid, BorderLayout.CENTER);
                }
                MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
            }
        });
        return itemInfoPanel;
    }

    private void closeGridItem(@NonNull ItemTradeNotificationDescriptor message) {
        String nickname = message.getWhisperNickname();
        ItemInfoPanel itemInfoPanel = tabButtons.get(nickname + message.getTabName());
        if (itemInfoPanel != null) {
            if (itemInfoPanel.getStashTabDescriptor().isUndefined()) {
                itemInfoPanel.getStashTabDescriptor().setUndefined(false);
                stashTabsContainer.addTab(itemInfoPanel.getStashTabDescriptor());
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
            MercuryStoreUI.repaintSubject.onNext(ItemsGridFrame.class);
        }
    }
}
