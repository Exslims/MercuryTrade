package com.mercury.platform.ui.components.panel.grid;

import com.mercury.platform.shared.entity.StashTab;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;

public class TabInfoPanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    private StashTab stashTab;

    public TabInfoPanel(@NonNull StashTab stashTab, @NonNull ComponentsFactory componentsFactory) {
        this.stashTab = stashTab;
        this.componentsFactory = componentsFactory;
        createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(AppThemeColor.FRAME);
        JButton hideButton = componentsFactory.getIconButton("app/close.png", 12, AppThemeColor.FRAME_ALPHA, "Dismiss");
        hideButton.addActionListener((action)->{
            stashTab.setUndefined(true);
            MercuryStoreUI.INSTANCE.dismissTabInfoPanelSubject.onNext(this);
        });
        JPanel tabInfoPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel tabLabel = componentsFactory.getTextLabel(stashTab.getTitle());
        tabLabel.setBorder(null);
        tabLabel.setFont(componentsFactory.getFont(FontStyle.BOLD,15f));
        tabInfoPanel.add(tabLabel);
        JCheckBox isItQuad = componentsFactory.getCheckBox("Is it Quad?");
        isItQuad.setSelected(stashTab.isQuad());
        isItQuad.addActionListener(action->{
            stashTab.setQuad(isItQuad.isSelected());
        });
        tabInfoPanel.add(isItQuad);
        tabInfoPanel.add(hideButton);

        this.add(tabInfoPanel,BorderLayout.PAGE_END);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER, 1));
    }

    public StashTab getStashTab() {
        return stashTab;
    }

    public void setStashTab(StashTab stashTab) {
        this.stashTab = stashTab;
    }
}
