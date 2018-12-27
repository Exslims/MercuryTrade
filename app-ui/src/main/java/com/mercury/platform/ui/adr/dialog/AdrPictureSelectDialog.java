package com.mercury.platform.ui.adr.dialog;


import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.ui.adr.components.panel.ui.PicturesListCellRenderer;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdrPictureSelectDialog extends AdrSelectDialog {
    public AdrPictureSelectDialog() {
        super();
        this.setTitle("Select picture");
    }

    @Override
    protected void createView() {
        this.config = Configuration.get().pictureBundleConfiguration();
        this.setPreferredSize(new Dimension(530, 400));

        VerticalScrollContainer container = new VerticalScrollContainer();
        container.setBackground(AppThemeColor.SLIDE_BG);
        container.setLayout(new BorderLayout());

        JScrollPane scrollPane = this.componentsFactory.getVerticalContainer(container);
        List<String> entities = this.config.getDefaultBundle();
        entities.addAll(this.config.getEntities());
        this.iconsList = new JList<>(entities.toArray());
        this.iconsList.setBackground(AppThemeColor.SLIDE_BG);
        this.iconsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.iconsList.setVisibleRowCount(-1);
        this.iconsList.setCellRenderer(new PicturesListCellRenderer());
        this.iconsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        container.add(this.iconsList, BorderLayout.CENTER);

        scrollPane.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER_DARK));
        this.add(this.getFilterPanel(), BorderLayout.PAGE_START);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(this.getBottomPanel(), BorderLayout.PAGE_END);
    }
}
