package com.mercury.platform.ui.adr.components.panel.tree;

import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AdrMouseOverListener extends MouseAdapter {
    private AdrNodePanel source;

    public AdrMouseOverListener(AdrNodePanel source) {
        this.source = source;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        this.source.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
        MercuryStoreUI.adrSelectSubject.onNext(this.source.getDescriptor());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.source.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_MESSAGE));
        this.source.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.source.setBorder(BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.MSG_HEADER_BORDER));
        this.source.repaint();
    }
}
