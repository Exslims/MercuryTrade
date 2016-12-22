package com.home.clicker.ui.components.panel;

import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.ChatCommandEvent;
import com.home.clicker.shared.events.custom.CopyToClipboardEvent;
import com.home.clicker.shared.events.custom.OpenChatEvent;
import com.home.clicker.ui.components.ComponentsFactory;
import com.home.clicker.ui.components.fields.ExButton;
import com.home.clicker.ui.components.fields.label.FontStyle;
import com.home.clicker.ui.components.fields.label.TextAlignment;
import com.home.clicker.ui.misc.AppThemeColor;
import com.home.clicker.ui.misc.CustomButtonFactory;
import org.apache.commons.lang3.StringUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 15.12.2016.
 */
public class MessagePanel extends JPanel {
    private ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    private List<String> supportedIcons;

    private String whisper;
    private String message;

    private JLabel newLabel;
    private JLabel itemLabel;
    public MessagePanel(String whisper, String message) {
        super(new BorderLayout());
        supportedIcons = new ArrayList<>();
        supportedIcons.addAll(Collections.singletonList("chaos,exalted,fusing,vaal"));

        this.whisper = whisper;
        this.message = message;
        init();
    }
    //todo resizing
    private void init(){
        this.setBackground(AppThemeColor.TRANSPARENT);

        JLabel whisperLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP,15f,whisper + ":");
        Border border = whisperLabel.getBorder();
        whisperLabel.setBorder(new CompoundBorder(border,new EmptyBorder(10,5,0,10)));

        newLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_IMPORTANT,TextAlignment.LEFTOP,16f,"NEW");
        border = newLabel.getBorder();
        newLabel.setBorder(new CompoundBorder(border,new EmptyBorder(10,5,0,25)));

        JPanel topPanel = new JPanel(new BorderLayout());
        String tabName = StringUtils.substringBetween(message, "(stash tab ", "; position:");
        if(tabName != null) {
            JLabel tabNameLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MISC,TextAlignment.LEFTOP,15f,"Tab: " + tabName);
            border = tabNameLabel.getBorder();
            tabNameLabel.setBorder(new CompoundBorder(border, new EmptyBorder(10, 5, 0, 25)));
            topPanel.add(tabNameLabel,BorderLayout.CENTER);
        }

        topPanel.setBackground(AppThemeColor.TRANSPARENT);
        topPanel.add(whisperLabel,BorderLayout.LINE_START);
        topPanel.add(newLabel,BorderLayout.LINE_END);

        this.add(topPanel,BorderLayout.PAGE_START);

        Dimension rectSize = new Dimension();
        rectSize.setSize(350, 114);
        this.setMaximumSize(rectSize);
        this.setMinimumSize(rectSize);
        this.setPreferredSize(rectSize);

        this.add(getFormattedMessagePanel(message),BorderLayout.CENTER);

        JPanel buttonsPanel = CustomButtonFactory.getButtonsPanel(whisper);
        int buttonsCount = buttonsPanel.getComponentCount();
        if(buttonsCount > 5) {
            buttonsPanel.setSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
            buttonsPanel.setPreferredSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
            buttonsPanel.setMinimumSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
        }
        ExButton invite = new ExButton("invite");
        invite.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventRouter.fireEvent(new ChatCommandEvent("/invite " + whisper));

                Timer timer = new Timer(500,null);
                timer.addActionListener(event -> {
                    EventRouter.fireEvent(new CopyToClipboardEvent(itemLabel.getText()));
                    timer.stop();
                });
                timer.start();

            }
        });
        buttonsPanel.add(invite,0);

        BufferedImage buttonIcon = null;
        try {
            buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("app/openChat.png"));
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
        JPanel labelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelsPanel.setBackground(AppThemeColor.TRANSPARENT);

        String itemName = StringUtils.substringBetween(message, "to buy your ", " listed for");
        if(itemName == null){
            itemName = StringUtils.substringBetween(message, "to buy your ", " for my");
        }
        itemLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_IMPORTANT,TextAlignment.LEFTOP,17f,itemName);
        labelsPanel.add(itemLabel);

        JLabel secondPart = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_MESSAGE,TextAlignment.LEFTOP,16f,"listed for");
        labelsPanel.add(secondPart);

        String price = StringUtils.substringBetween(message, "listed for ", " in ");
        if(price == null){
            price = StringUtils.substringBetween(message, "for my ", " in ");
        }
        if(price != null) {
            String[] split = price.split(" ");
            JLabel priceLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE,TextAlignment.LEFTOP,17f,split[0]);
            JLabel currencyLabel = null;
            if(supportedIcons.contains(split[1])){
                currencyLabel = componentsFactory.getIconLabel("currency/" + split[1] + ".png",20);
            }else
                currencyLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE,TextAlignment.LEFTOP,17f,split[1]);
            labelsPanel.add(priceLabel);
            labelsPanel.add(currencyLabel);
        }

        String offer = StringUtils.substringAfterLast(message, "in Breach."); //todo
        String tabName = StringUtils.substringBetween(message, "(stash tab ", "; position:");
        if(tabName !=null ){
            offer = StringUtils.substringAfter(message, ")");
        }
        JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_MESSAGE,TextAlignment.LEFTOP,16f,offer);
        labelsPanel.add(offerLabel);
        if(offer.length() > 1){
            Dimension rectSize = new Dimension();
            rectSize.setSize(350, 130);
            this.setMaximumSize(rectSize);
            this.setMinimumSize(rectSize);
            this.setPreferredSize(rectSize);
        }
        return labelsPanel;
    }
    public void viewed(){
        newLabel.setText("");
        MessagePanel.this.revalidate();
        MessagePanel.this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(0,0,getWidth(),getHeight());
    }
}
