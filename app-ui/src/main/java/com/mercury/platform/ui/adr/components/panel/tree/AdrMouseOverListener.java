package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.store.DestroySubscription;
import com.mercury.platform.ui.adr.routing.AdrComponentDefinition;
import com.mercury.platform.ui.adr.routing.AdrComponentOperations;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.Setter;
import rx.Subscription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AdrMouseOverListener<T extends AdrComponentDescriptor> extends MouseAdapter implements DestroySubscription {
    private JComponent source;
    private T descriptor;
    private Cursor overCursor = new Cursor(Cursor.HAND_CURSOR);
    private boolean clicked = false;
    @Setter
    private boolean processSelect = true;
    @Setter
    private boolean fromGroup = true;

    private Subscription adrSelectSubscription;

    public AdrMouseOverListener(JComponent source, T descriptor, boolean fromGroup) {
        this.fromGroup = fromGroup;
        this.source = source;
        this.descriptor = descriptor;
        this.adrSelectSubscription = MercuryStoreUI.adrSelectSubject.subscribe(selected -> {
            if (!descriptor.equals(selected)) {
                this.source.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                this.clicked = false;
            } else {
                this.source.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER));
                this.clicked = true;
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.source.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER));
        if (this.processSelect) {
            this.clicked = !this.clicked;
            this.source.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_SELECTED_BORDER));
            MercuryStoreUI.adrSelectSubject.onNext(this.descriptor);
            MercuryStoreUI.adrComponentStateSubject.onNext(
                    new AdrComponentDefinition(
                            this.descriptor,
                            AdrComponentOperations.EDIT_COMPONENT,
                            this.fromGroup));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!this.clicked) {
            this.source.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, AppThemeColor.ADR_MOUSE_OVER_BORDER));
        }
        this.source.setCursor(this.overCursor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!this.clicked) {
            this.source.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
        this.source.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void onDestroy() {
        this.adrSelectSubscription.unsubscribe();
    }
}
