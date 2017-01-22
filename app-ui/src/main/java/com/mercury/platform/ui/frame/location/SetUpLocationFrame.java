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
        processingSaveLocAndSize = false;
        this.setMinimumSize(new Dimension(470,40));
    }

    @Override
    protected void initialize() {
        super.initialize();
        JPanel rootPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(6,6,0,6));

        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setPreferredSize(new Dimension(450,40));
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.add(componentsFactory.getSimpleTextAre("Movers unlocked. Move them now and click Lock when you are done."),BorderLayout.CENTER);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton lock = componentsFactory.getBorderedButton("Lock");
        lock.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FramesManager.INSTANCE.enableMovement();
                SetUpLocationFrame.this.setVisible(false);
            }
        });
        lock.setPreferredSize(new Dimension(80, 26));

        JButton test = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME,
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.BORDER),
                        BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 3)
                ),
                "Test",
                14f);
        test.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            }
        });
        test.setPreferredSize(new Dimension(80, 26));

        miscPanel.add(test);
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
