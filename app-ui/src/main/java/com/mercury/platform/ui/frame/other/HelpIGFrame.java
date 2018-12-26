package com.mercury.platform.ui.frame.other;

import com.mercury.platform.ui.frame.AbstractOverlaidFrame;

import javax.swing.*;
import java.awt.*;

public class HelpIGFrame extends AbstractOverlaidFrame {
    private int MARGIN = 400;
    public HelpIGFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.componentsFactory.setScale(1.1f);
    }

    @Override
    public void subscribe() {
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    @Override
    public void onViewInit() {
        this.setOpacity(this.applicationConfig.get().getMaxOpacity() / 100f);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        ImageIcon icon = this.componentsFactory.getImage("app/helpIGImg.png");
        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(dim.width - MARGIN, -1,  java.awt.Image.SCALE_SMOOTH);
        if(dim.height-MARGIN < newimg.getHeight(null)) {
            newimg = image.getScaledInstance(-1, dim.height - MARGIN,  java.awt.Image.SCALE_SMOOTH);
        }
        icon = new ImageIcon(newimg);

        JLabel img = new JLabel(icon);
        this.add(img);
        this.pack();
        this.setLocation(dim.width/2-this.getSize().width/2, 0);
    }
}
