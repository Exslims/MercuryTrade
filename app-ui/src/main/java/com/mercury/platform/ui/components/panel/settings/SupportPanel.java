package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Константин on 11.02.2017.
 */
public class SupportPanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    public SupportPanel() {
        super();
        componentsFactory = new ComponentsFactory();
        this.setBackground(AppThemeColor.TRANSPARENT);
        createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel donatePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton donate = componentsFactory.getIconifiedTransparentButton("app/paypal.png","Donate");
        donate.setBackground(AppThemeColor.SLIDE_BG);
        donate.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=HJVSYP4YR7V88&lc=US&item_name=MercuryTrade&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
        donatePanel.add(donate);
        this.add(donatePanel);
        JTextArea donateText = componentsFactory.getSimpleTextAre("We aimed to create a convenience tool in form of an easy-to-use application, primarily for trading purposes. If MercuryTrade successfully managed to save your time or improve your experience, you can thank us by donating and telling your friends.");
        donateText.setPreferredSize(new Dimension(300,150));
        JPanel donateTextPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        donateTextPanel.add(donateText);
        this.add(donateTextPanel);
    }
}
