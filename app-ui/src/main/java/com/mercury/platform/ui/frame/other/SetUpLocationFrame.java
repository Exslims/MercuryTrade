package com.mercury.platform.ui.frame.other;

import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SetUpLocationFrame extends AbstractOverlaidFrame {
    public SetUpLocationFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));
    }

    @Override
    public void onViewInit() {

        JPanel rootPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.add(componentsFactory.getSimpleTextArea("Panels UNLOCKED Move them and click Lock when done."), BorderLayout.CENTER);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton restoreDefault = componentsFactory.getBorderedButton("Restore defaults");
        restoreDefault.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 3)
        ));
        restoreDefault.setBackground(AppThemeColor.FRAME);

        restoreDefault.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FramesManager.INSTANCE.restoreDefaultLocation();
            }
        });
        restoreDefault.setPreferredSize(new Dimension(140, 26));

        JButton lock = componentsFactory.getBorderedButton("Lock!");
        lock.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FramesManager.INSTANCE.disableMovement();
                SetUpLocationFrame.this.setVisible(false);
            }
        });
        lock.setPreferredSize(new Dimension(100, 26));

        miscPanel.add(restoreDefault);
        miscPanel.add(lock);
        rootPanel.add(root, BorderLayout.CENTER);
        this.add(rootPanel, BorderLayout.CENTER);
        this.add(miscPanel, BorderLayout.PAGE_END);
        this.setPreferredSize(new Dimension(300, 100));
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 4 - this.getSize().height / 2);
    }

    @Override
    public void subscribe() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
