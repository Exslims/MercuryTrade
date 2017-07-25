package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AdrMouseOverListener<T extends AdrComponentDescriptor> extends MouseAdapter {
    private JPanel source;
    private T descriptor;
    private boolean clicked = false;

    public AdrMouseOverListener(JPanel source, T descriptor) {
        this.source = source;
        this.descriptor = descriptor;
        MercuryStoreUI.adrSelectSubject.subscribe(selected -> {
            if(!descriptor.equals(selected)){
                this.source.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, AppThemeColor.ADR_DEFAULT_BORDER));
                this.clicked = false;
            }else {
                this.source.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER));
                this.clicked = true;
            }
        });
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        this.source.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER));
        this.clicked = !this.clicked;
        MercuryStoreUI.adrSelectSubject.onNext(this.descriptor);
        MercuryStoreUI.adrComponentStateSubject.onNext(
                        new AdrComponentDefinition(
                               this.descriptor,
                                AdrComponentOperations.EDIT_COMPONENT,
                                true));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(!this.clicked) {
            this.source.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, AppThemeColor.ADR_MOUSE_OVER_BORDER));
        }
        this.source.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(!this.clicked) {
            this.source.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, AppThemeColor.ADR_DEFAULT_BORDER));
        }
        this.source.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
