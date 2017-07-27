package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;


public class AdrDialogGroupNodePanel extends AdrNodePanel<AdrTrackerGroupDescriptor>{
    private JLabel groupLabel;
    private JPanel container;
    public AdrDialogGroupNodePanel(AdrTrackerGroupDescriptor descriptor) {
        super(descriptor);
        this.mouseListener.setProcessSelect(false);
    }

    @Override
    protected void update() {
        this.groupLabel.setText(descriptor.getTitle());
        this.groupLabel.setIcon(componentsFactory.getIcon(this.adrComponentsFactory.getGroupTypeIconPath(this.descriptor), 48));
    }

    @Override
    public void createUI() {
        this.container = this.componentsFactory.getJPanel(new GridLayout(descriptor.getCells().size(), 1));
        this.container.setVisible(false);
        this.add(this.container,BorderLayout.CENTER);
        this.add(this.getTopPanel(),BorderLayout.PAGE_START);
    }

    @Override
    public Component add(Component comp) {
        return this.container.add(comp);
    }

    private JPanel getTopPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        JButton expandButton = this.componentsFactory.getIconButton("app/adr/node_expand.png", 20, AppThemeColor.FRAME, "");
        expandButton.addActionListener(action -> {
            if(this.container.isVisible()){
                expandButton.setIcon(this.componentsFactory.getIcon("app/adr/node_expand.png",20));
                this.container.setVisible(false);
            }else {
                expandButton.setIcon(this.componentsFactory.getIcon("app/adr/node_collapse.png",20));
                this.container.setVisible(true);
            }
            MercuryStoreUI.adrUpdateTree.onNext(true);
        });
        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 15, AppThemeColor.FRAME, TooltipConstants.ADR_REMOVE_BUTTON);

        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(1, 1));
        buttonsPanel.add(removeButton);

        this.groupLabel = new JLabel();
        this.groupLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.groupLabel.setBackground(AppThemeColor.FRAME);
        this.groupLabel.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
        this.groupLabel.setText(descriptor.getTitle());
        this.groupLabel.setPreferredSize(new Dimension(170, 30));
        this.groupLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 2));

        root.add(expandButton,BorderLayout.LINE_START);
        root.add(this.groupLabel,BorderLayout.CENTER);
        root.add(buttonsPanel,BorderLayout.LINE_END);
        return root;
    }
}
