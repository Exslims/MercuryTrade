package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.style.MercuryScrollBarUI;
import com.mercury.platform.ui.components.panel.VerticalScrollContainer;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.titled.SettingsFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.net.URI;
import java.util.*;
import java.util.List;

public class SupportPanel extends JPanel implements HasUI{
    private ComponentsFactory componentsFactory;
    public SupportPanel() {
        super();
        componentsFactory = new ComponentsFactory();
        this.setBackground(AppThemeColor.SLIDE_BG);
        this.createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new BorderLayout());

        JPanel donatePanel = componentsFactory.getTransparentPanel();
        donatePanel.setLayout(new BoxLayout(donatePanel,BoxLayout.Y_AXIS));
        donatePanel.setBackground(AppThemeColor.SLIDE_BG);

        JButton donate = componentsFactory.getIconifiedTransparentButton("app/paypal.png","Donate");
        donate.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
        donate.setBackground(AppThemeColor.SLIDE_BG);
        donate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                donate.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                donate.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.paypal.me/mercurytrade"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        JTextArea donateText = componentsFactory.getSimpleTextArea("We aimed to create a convenience tool in form of an easy-to-use application, primarily for trading purposes. If MercuryTrade successfully managed to save your time or improve your experience, you can thank us by donating and telling your friends.");
        donateText.setPreferredSize(new Dimension(300,150));
        JPanel donateTextPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel donateButtonPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        donateButtonPanel.setBorder(BorderFactory.createEmptyBorder(30,0,0,0));
        donateTextPanel.add(donateText);
        donateButtonPanel.add(donate);
        donatePanel.add(donateButtonPanel);
        donatePanel.add(donateTextPanel);
        this.add(donatePanel,BorderLayout.CENTER);
        this.add(getDonationsPanel(),BorderLayout.LINE_END);
    }
    private JPanel getDonationsPanel() {
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(20,0,10,20));
        root.add(componentsFactory.getTextLabel("Thanks a lot for support:          "),BorderLayout.PAGE_START);

        JPanel donationsList = new VerticalScrollContainer();
        donationsList.setBackground(AppThemeColor.TRANSPARENT);
        donationsList.setLayout(new BoxLayout(donationsList,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(donationsList);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppThemeColor.FRAME);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                MercuryStoreUI.repaintSubject.onNext(SettingsFrame.class);
            }
        });
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setBackground(AppThemeColor.SLIDE_BG);
        vBar.setUI(new MercuryScrollBarUI());
        vBar.setPreferredSize(new Dimension(14, Integer.MAX_VALUE));
        vBar.setUnitIncrement(3);
        vBar.setBorder(BorderFactory.createEmptyBorder(1,1,1,2));
        vBar.addAdjustmentListener(e -> repaint());
        donationsList.getParent().setBackground(AppThemeColor.TRANSPARENT);

        getDonations().forEach(pair -> {
            JPanel item = componentsFactory.getTransparentPanel(new BorderLayout());
            item.add(componentsFactory.getTextLabel(pair.name, FontStyle.REGULAR,pair.color),BorderLayout.CENTER);
            donationsList.add(item);
        });

        root.add(scrollPane,BorderLayout.CENTER);
        return root;
    }
    private List<DonationPair> getDonations(){
        List<DonationPair> donations = new ArrayList<>();
        donations.add(new DonationPair("222Craft",AppThemeColor.TEXT_IMPORTANT));
        donations.add(new DonationPair("StubenZocker",AppThemeColor.TEXT_DEFAULT));
        donations.add(new DonationPair("SirKultan",AppThemeColor.TEXT_DEFAULT));
        return donations;
    }

    private class DonationPair{
        private String name;
        private Color color;

        DonationPair(String name, Color color) {
            this.name = name;
            this.color = color;
        }
    }
}
