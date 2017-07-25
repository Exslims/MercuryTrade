package com.mercury.platform.ui.adr.dialog;


import com.google.gson.Gson;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.components.panel.tree.*;
import com.mercury.platform.ui.adr.components.panel.tree.dialog.AdrDialogTreeNodeRenderer;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AdrExportDialog<T extends AdrComponentDescriptor> extends AdrDialog<T> {
    private JTextArea jsonArea;
    public AdrExportDialog(Component relative, java.util.List<T> payload) {
        super(relative, payload);
        this.setTitle("Export manager");

        MercuryStoreUI.adrUpdateTree.subscribe(state -> {
            this.repaint();
        });
    }

    @Override
    protected void createView() {
        this.setPreferredSize(new Dimension(600,500));
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.add(this.getDataPanel(),BorderLayout.CENTER);
        root.add(this.getViewPanel(),BorderLayout.LINE_END);
        this.add(this.componentsFactory.wrapToSlide(root),BorderLayout.CENTER);
    }
    private JPanel getDataPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        root.setBackground(AppThemeColor.ADR_BG);
        JLabel header = this.componentsFactory.getTextLabel("Data (Ctrl + A for copy):", FontStyle.BOLD,18);
        header.setForeground(AppThemeColor.TEXT_NICKNAME);
        root.add(header,BorderLayout.PAGE_START);

        JPanel container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.ADR_BG);
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        JScrollPane verticalContainer = this.componentsFactory.getVerticalContainer(container);

        this.jsonArea = this.componentsFactory.getSimpleTextArea(this.getPayloadAsJson());
        this.jsonArea.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.jsonArea.setMinimumSize(new Dimension(450,550));
        this.jsonArea.setBackground(AppThemeColor.ADR_TEXT_ARE_BG);
        container.add(this.jsonArea,AppThemeColor.ADR_BG);
        root.add(this.componentsFactory.wrapToSlide(verticalContainer,AppThemeColor.ADR_BG,0,5,4,0),BorderLayout.CENTER);
        return this.componentsFactory.wrapToSlide(root);
    }
    private JPanel getViewPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setPreferredSize(new Dimension(240,100));
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        root.setBackground(AppThemeColor.FRAME_RGB);
        JPanel headerPanel = this.componentsFactory.getJPanel(new BorderLayout());
        headerPanel.setBackground(AppThemeColor.ADR_BG);

        JButton addButton = this.componentsFactory.getIconButton("app/adr/add_node.png", 15, AppThemeColor.FRAME, TooltipConstants.ADR_ADD_BUTTON);
        addButton.setBackground(AppThemeColor.ADR_BG);
        JLabel header = this.componentsFactory.getTextLabel("View:", FontStyle.BOLD,18);
        header.setForeground(AppThemeColor.TEXT_NICKNAME);
        headerPanel.add(header,BorderLayout.CENTER);
        headerPanel.add(addButton,BorderLayout.LINE_END);
        root.add(headerPanel,BorderLayout.PAGE_START);

        root.add(this.componentsFactory.wrapToSlide(new AdrTreePanel(
                (List<AdrComponentDescriptor>) this.payload,
                new AdrDialogTreeNodeRenderer()),AppThemeColor.FRAME_RGB),BorderLayout.CENTER);
        return this.componentsFactory.wrapToSlide(root);
    }
    private String getPayloadAsJson(){
        return new Gson().toJson(this.payload);
    }
}
