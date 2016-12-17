package com.home.clicker.ui.components;

import com.home.clicker.events.EventRouter;
import com.home.clicker.events.custom.ChatCommandEvent;
import com.home.clicker.events.custom.OpenChatEvent;
import com.home.clicker.ui.misc.AppThemeColor;
import com.home.clicker.ui.misc.CustomButtonFactory;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Константин on 15.12.2016.
 */
public class MessagePanel extends JPanel {
    private String whisper;
    private String message;

    private ExLabel newLabel;
    public MessagePanel(String whisper, String message) {
        super(new BorderLayout());
        this.whisper = whisper;
        this.message = message;
        init();
    }
    //todo resizing
    private void init(){
        this.setBackground(AppThemeColor.TRANSPARENT);
        ExLabel whisperLabel = new ExLabel(whisper + ":");
        whisperLabel.setForeground(AppThemeColor.TEXT_NICKNAME);
//        whisperLabel.setPreferredSize(new Dimension(Integer.MAX_VALUE,20));
        Border border = whisperLabel.getBorder();
        whisperLabel.setBorder(new CompoundBorder(border,new EmptyBorder(10,5,10,10)));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppThemeColor.TRANSPARENT);

        topPanel.add(whisperLabel,BorderLayout.CENTER);

        newLabel = new ExLabel("NEW");
        newLabel.setForeground(AppThemeColor.TEXT_IMPORTANT);
        border = newLabel.getBorder();
        newLabel.setBorder(new CompoundBorder(border,new EmptyBorder(10,5,10,25)));
        topPanel.add(newLabel,BorderLayout.LINE_END);

        this.add(topPanel,BorderLayout.PAGE_START);
        Dimension rectSize = new Dimension();

        rectSize.setSize(350, 170);

        this.setMaximumSize(rectSize);
        this.setMinimumSize(rectSize);
        this.setPreferredSize(rectSize);

        this.add(getFormattedMessagePanel(message),BorderLayout.CENTER);

        JPanel buttonsPanel = CustomButtonFactory.getButtonsPanel(whisper);
        int buttonsCount = buttonsPanel.getComponentCount();
        if(buttonsCount > 4) {
            buttonsPanel.setSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
            buttonsPanel.setPreferredSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
            buttonsPanel.setMinimumSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
        }
        ExButton invite = new ExButton("invite");
        invite.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventRouter.fireEvent(new ChatCommandEvent("/invite " + whisper));
            }
        });
        buttonsPanel.add(invite,0);

        BufferedImage buttonIcon = null;
        try {
            buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("openChat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage icon = Scalr.resize(buttonIcon, 15);
        ExButton openChatButton = new ExButton(new ImageIcon(icon));
        openChatButton.setToolTipText("Open chat");
        openChatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventRouter.fireEvent(new OpenChatEvent(whisper));
            }
        });
        buttonsPanel.add(openChatButton);

        this.add(buttonsPanel,BorderLayout.PAGE_END);
    }

    private JPanel getFormattedMessagePanel(String message){
        JPanel labelsPanel = new JPanel(new FlowLayout());
        labelsPanel.setBackground(AppThemeColor.TRANSPARENT);


        ExLabel firstPartLabel = new ExLabel("Hi, i'd like to buy ");
        firstPartLabel.setForeground(AppThemeColor.TEXT_MESSAGE);
        labelsPanel.add(firstPartLabel);

        String itemName = StringUtils.substringBetween(message, "to buy your ", " listed for");
        if(itemName == null){
            itemName = StringUtils.substringBetween(message, "to buy your ", " for my");
        }
        ExLabel itemLabel = new ExLabel(itemName);
        itemLabel.setForeground(AppThemeColor.TEXT_IMPORTANT);
        labelsPanel.add(itemLabel);

        ExLabel secondPart = new ExLabel(" listed for ");
        secondPart.setForeground(AppThemeColor.TEXT_MESSAGE);
        labelsPanel.add(secondPart);

        String price = StringUtils.substringBetween(message, "listed for ", " in ");
        if(price == null){
            price = StringUtils.substringBetween(message, "for my ", " in ");
        }
        String[] split = price.split(" ");
        ExLabel currencyLabel = null;
        BufferedImage buttonIcon = null;
        try {
            switch (split[1]) {
                case "chaos": {
                    buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("Chaos_Orb.png"));
                    BufferedImage icon = Scalr.resize(buttonIcon, 20);
                    currencyLabel = new ExLabel(new ImageIcon(icon));
                    break;
                }
                case "exalted": {
                    buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("Exalted_Orb.png"));
                    BufferedImage icon = Scalr.resize(buttonIcon, 20);
                    currencyLabel = new ExLabel(new ImageIcon(icon));
                    break;
                }
                default:
                    currencyLabel = new ExLabel(split[1]);
                    currencyLabel.setForeground(AppThemeColor.TEXT_MESSAGE);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExLabel priceLabel = new ExLabel(split[0]);
        priceLabel.setForeground(AppThemeColor.TEXT_MESSAGE);
        labelsPanel.add(priceLabel);
        labelsPanel.add(currencyLabel);


        String lastPart = StringUtils.substringAfter(message, " in ");
        ExLabel lastPartLabel = new ExLabel(" in " + lastPart);
        lastPartLabel.setForeground(AppThemeColor.TEXT_MESSAGE);
        labelsPanel.add(lastPartLabel);
        return labelsPanel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.setColor(new Color(0,0,0,0));
        g.fillRect(0,0,getWidth(),getHeight());
    }
    public void viewed(){
        newLabel.setText("");
        MessagePanel.this.revalidate();
        MessagePanel.this.repaint();
    }
}
