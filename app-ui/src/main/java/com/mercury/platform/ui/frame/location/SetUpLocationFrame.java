package com.mercury.platform.ui.frame.location;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 22.01.2017.
 */
public class SetUpLocationFrame extends ComponentFrame {
    public SetUpLocationFrame() {
        super("MT-SetUpLocationFrame");
        this.setVisible(false);
        prevState = FrameStates.HIDE;
    }

    @Override
    protected void initialize() {
        super.initialize();
        JPanel rootPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(6,6,0,6));

        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setPreferredSize(new Dimension(225,30));
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.add(componentsFactory.getSimpleTextAre(" Panels are currently unlocked"),BorderLayout.CENTER);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton lock = componentsFactory.getBorderedButton("Lock!");
        lock.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FramesManager.INSTANCE.enableMovement();
                SetUpLocationFrame.this.setVisible(false);
            }
        });
        lock.setPreferredSize(new Dimension(100, 26));

        miscPanel.add(lock);
        rootPanel.add(root,BorderLayout.CENTER);
        this.add(rootPanel,BorderLayout.CENTER);
        this.add(miscPanel,BorderLayout.PAGE_END);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/4-this.getSize().height/2);
        this.pack();
    }

    @Override
    public void showComponent() {
        disableHideEffect();
        super.showComponent();
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
