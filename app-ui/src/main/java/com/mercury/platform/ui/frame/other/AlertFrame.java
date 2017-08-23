package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AlertFrame extends AbstractOverlaidFrame {
    private JLabel messageLabel;

    public AlertFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
    }

    @Override
    public void onViewInit() {
        JPanel messagePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        messageLabel = componentsFactory.getTextLabel("");
        messagePanel.add(messageLabel);

        JPanel closePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton close = componentsFactory.getBorderedButton("Ok");
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setVisible(false);
            }
        });
        close.setPreferredSize(new Dimension(80, 26));
        closePanel.add(close);
        this.add(messagePanel, BorderLayout.CENTER);
        this.add(closePanel, BorderLayout.PAGE_END);
        this.pack();
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.stringAlertSubject.subscribe(message -> {
            this.messageLabel.setText(message);
            this.repaint();
            this.pack();
            this.setVisible(true);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        });
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
