package com.mercury.platform.ui.adr.components.panel.tree.dialog;

import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.AdrNodePanel;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;


public class AdrDialogGroupNodePanel extends AdrNodePanel<AdrGroupDescriptor>{
    private JPanel container;
    public AdrDialogGroupNodePanel(AdrGroupDescriptor descriptor) {
        super(descriptor);
        this.mouseListener.setProcessSelect(false);
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

        JLabel label = new JLabel();
        label.setForeground(AppThemeColor.TEXT_DEFAULT);
        label.setBackground(AppThemeColor.FRAME);
        label.setFont(componentsFactory.getFont(FontStyle.REGULAR, 16));
        label.setText(descriptor.getTitle());
        String iconPath = "app/adr/static_group_icon.png";
        switch (descriptor.getGroupType()) {
            case STATIC: {
                iconPath = "app/adr/static_group_icon.png";
                break;
            }
            case DYNAMIC: {
                iconPath = "app/adr/dynamic_group_icon.png";
                break;
            }
        }
        label.setPreferredSize(new Dimension(170, 30));
        label.setIcon(componentsFactory.getIcon(iconPath, 48));
        label.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 2));
        root.add(expandButton,BorderLayout.LINE_START);
        root.add(label,BorderLayout.CENTER);
        root.add(buttonsPanel,BorderLayout.LINE_END);
        return root;
    }
}
