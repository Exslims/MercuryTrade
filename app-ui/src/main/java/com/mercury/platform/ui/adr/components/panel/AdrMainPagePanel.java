package com.mercury.platform.ui.adr.components.panel;


import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrGroupDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;

public class AdrMainPagePanel extends AdrPagePanel<AdrComponentDescriptor> {
    public AdrMainPagePanel() {
        super();
    }

    @Override
    protected void init() {
        if(this.payload instanceof AdrGroupDescriptor){
            this.add(this.componentsFactory.getTextLabel(this.payload.getTitle() + ">"), BorderLayout.PAGE_START);
        }
        JButton createGroup = this.componentsFactory.getButton("CREATE GROUP");
        createGroup.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
        createGroup.setPreferredSize(new Dimension(220,50));
        createGroup.addActionListener(action -> {
            MercuryStoreUI.adrComponentStateSubject.onNext(
                    new AdrComponentDefinition(new AdrGroupDescriptor(), AdrComponentOperations.NEW_COMPONENT,false)
            );
        });
        JButton createIcon = this.componentsFactory.getButton("CREATE ICON");
        createIcon.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
        createIcon.setPreferredSize(new Dimension(220,50));
        createIcon.addActionListener(action -> {
            AdrComponentDefinition definition = new AdrComponentDefinition();
            AdrIconDescriptor iconDescriptor = new AdrIconDescriptor();
            definition.setDescriptor(iconDescriptor);
            definition.setOperations(AdrComponentOperations.NEW_COMPONENT);
            if(this.payload instanceof AdrGroupDescriptor) {
                ((AdrGroupDescriptor) this.payload).getCells().add(iconDescriptor);
                definition.setFromGroup(true);
            }
            MercuryStoreUI.adrComponentStateSubject.onNext(definition);
        });
        JButton createPB = this.componentsFactory.getButton("CREATE PROGRESS BAR");
        createPB.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
        createPB.setPreferredSize(new Dimension(220,50));
        createPB.addActionListener(action -> {
            AdrComponentDefinition definition = new AdrComponentDefinition();
            AdrProgressBarDescriptor iconDescriptor = new AdrProgressBarDescriptor();
            definition.setDescriptor(iconDescriptor);
            definition.setOperations(AdrComponentOperations.NEW_COMPONENT);
            if(this.payload instanceof AdrGroupDescriptor) {
                ((AdrGroupDescriptor) this.payload).getCells().add(iconDescriptor);
                definition.setFromGroup(true);
            }
            MercuryStoreUI.adrComponentStateSubject.onNext(definition);
        });

        JPanel buttonsPanel = this.componentsFactory.getJPanel(new GridLayout(8, 1));
        if(!(this.payload instanceof AdrGroupDescriptor)){
            buttonsPanel.add(wrap(createGroup));
        }
        buttonsPanel.add(wrap(createIcon));
        buttonsPanel.add(wrap(createPB));

        this.add(buttonsPanel,BorderLayout.CENTER);
    }
    private JPanel wrap(JButton button){
        JPanel panel = this.componentsFactory.getJPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(button);
        return panel;
    }
}
