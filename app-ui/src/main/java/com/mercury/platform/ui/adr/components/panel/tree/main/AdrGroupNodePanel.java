package com.mercury.platform.ui.adr.components.panel.tree.main;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.adr.components.panel.tree.model.AdrTreeNode;
import com.mercury.platform.ui.adr.routing.AdrPageDefinition;
import com.mercury.platform.ui.adr.routing.AdrPageState;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.dialog.AlertDialog;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;

public class AdrGroupNodePanel extends AdrNodePanel<AdrTrackerGroupDescriptor> {
    private JLabel groupLabel;
    private JPanel container;
    private JButton expandButton;

    public AdrGroupNodePanel(AdrTreeNode<AdrComponentDescriptor> treeNode) {
        super(treeNode);
    }

    @Override
    protected void update() {
        this.groupLabel.setText(descriptor.getTitle());
        this.groupLabel.setIcon(componentsFactory.getIcon(this.adrComponentsFactory.getGroupTypeIconPath(this.descriptor), 36));
        this.updateUI();
    }


    @Override
    public void onViewInit() {
        MercuryStoreUI.adrSelectSubject.subscribe(descriptor -> {
            if (this.descriptor.equals(descriptor)) {
                this.container.setVisible(true);
                this.expandButton.setIcon(this.componentsFactory.getIcon("app/adr/node_collapse.png", 16));
            }
        });
        this.container = this.componentsFactory.getJPanel(new GridLayout(0, 1));
        this.container.setVisible(false);

        this.add(this.container, BorderLayout.CENTER);
        this.add(this.getTopPanel(), BorderLayout.PAGE_START);
    }

    @Override
    public Component add(Component comp) {
        return this.container.add(comp);
    }

    @Override
    public void remove(Component comp) {
        this.container.remove(comp);
    }

    private JPanel getTopPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        this.expandButton = this.componentsFactory.getIconButton("app/adr/node_expand.png", 16, AppThemeColor.FRAME, "");
        this.expandButton.addActionListener(action -> {
            if (this.container.isVisible()) {
                this.expandButton.setIcon(this.componentsFactory.getIcon("app/adr/node_expand.png", 16));
                this.container.setVisible(false);
            } else {
                this.expandButton.setIcon(this.componentsFactory.getIcon("app/adr/node_collapse.png", 16));
                this.container.setVisible(true);
            }
            MercuryStoreUI.adrManagerPack.onNext(true);
        });
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 14, AppThemeColor.FRAME, TooltipConstants.ADR_REMOVE_BUTTON);
        removeButton.addActionListener(action -> new AlertDialog(success -> {
            if (success) {
                MercuryStoreUI.adrRemoveComponentSubject.onNext(descriptor);
                MercuryStoreUI.adrManagerPack.onNext(true);
            }
        }, "Do you want to delete this component?", this).setVisible(true));
        JButton addButton = this.componentsFactory.getIconButton("app/adr/add_node.png", 14, AppThemeColor.FRAME, TooltipConstants.ADR_ADD_BUTTON);
        addButton.addActionListener(action -> {
            MercuryStoreUI.adrSelectSubject.onNext(this.descriptor);
            MercuryStoreUI.adrStateSubject.onNext(new AdrPageDefinition<>(AdrPageState.MAIN, this.descriptor));
        });
        JButton exportButton = this.componentsFactory.getIconButton("app/adr/export_node.png", 14, AppThemeColor.FRAME, TooltipConstants.ADR_EXPORT_BUTTON);
        exportButton.addActionListener(action -> {
            MercuryStoreUI.adrExportSubject.onNext(this.descriptor);
        });
        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(1, 3));
        buttonsPanel.add(addButton);
        buttonsPanel.add(exportButton);
        buttonsPanel.add(removeButton);

        this.groupLabel = new JLabel();
        this.groupLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.groupLabel.setBackground(AppThemeColor.FRAME);
        this.groupLabel.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
        this.groupLabel.setText(descriptor.getTitle());

        this.groupLabel.setPreferredSize(new Dimension(170, 30));
        this.groupLabel.setIcon(componentsFactory.getIcon(this.adrComponentsFactory.getGroupTypeIconPath(this.descriptor), 36));
        this.groupLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 2));
        root.add(this.expandButton, BorderLayout.LINE_START);
        root.add(this.groupLabel, BorderLayout.CENTER);
        root.add(buttonsPanel, BorderLayout.LINE_END);
        return root;
    }
}
