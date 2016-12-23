package com.home.clicker.ui.components.panel;

import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.ChatCommandEvent;
import com.home.clicker.shared.events.custom.CopyToClipboardEvent;
import com.home.clicker.shared.events.custom.OpenChatEvent;
import com.home.clicker.ui.components.ComponentsFactory;
import com.home.clicker.ui.components.fields.label.FontStyle;
import com.home.clicker.ui.components.fields.label.TextAlignment;
import com.home.clicker.ui.misc.AppThemeColor;
import com.home.clicker.ui.misc.CustomButtonFactory;
import com.home.clicker.ui.misc.MessageParser;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by Константин on 15.12.2016.
 */
public class MessagePanel extends JPanel {
    private ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    private List<String> supportedIcons;

    private String whisper;
    private Map<String,String> parsedMessage;

    private JLabel newLabel;
    private JLabel itemLabel;
    public MessagePanel(String whisper, String message) {
        super(new BorderLayout());
        supportedIcons = new ArrayList<>();
        supportedIcons.addAll(Arrays.asList("chaos","exalted","fusing","vaal"));

        this.whisper = whisper;
        this.parsedMessage = MessageParser.parse(message);
        init();
    }
    //todo resizing
    private void init(){
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER,1));
        newLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_IMPORTANT,TextAlignment.LEFTOP,16f,"NEW");
        Border border = newLabel.getBorder();
        newLabel.setBorder(new CompoundBorder(border,new EmptyBorder(10,5,0,25)));

        JPanel topPanel = getWhisperPanel();
//        String tabName = parsedMessage.get("tabName");
//        if(tabName != null) {
//            JLabel tabNameLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MISC,TextAlignment.LEFTOP,15f,"Tab: " + tabName);
//            border = tabNameLabel.getBorder();
//            tabNameLabel.setBorder(new CompoundBorder(border, new EmptyBorder(10, 5, 0, 25)));
//            topPanel.add(tabNameLabel,BorderLayout.CENTER);
//        }

//        topPanel.add(getWhisperPanel(),BorderLayout.LINE_START);
//        topPanel.add(newLabel,BorderLayout.LINE_END);

        this.add(topPanel,BorderLayout.PAGE_START);

        Dimension rectSize = new Dimension();
        rectSize.setSize(350, 114);
        this.setMaximumSize(rectSize);
        this.setMinimumSize(rectSize);
        this.setPreferredSize(rectSize);

        this.add(getFormattedMessagePanel(),BorderLayout.CENTER);

        JPanel buttonsPanel = CustomButtonFactory.getButtonsPanel(whisper);
        int buttonsCount = buttonsPanel.getComponentCount();
        if(buttonsCount > 5) {
            buttonsPanel.setSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
            buttonsPanel.setPreferredSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
            buttonsPanel.setMinimumSize(new Dimension(buttonsPanel.getPreferredSize().width, (int)(buttonsPanel.getPreferredSize().height * 1.7)));
        }

        this.add(buttonsPanel,BorderLayout.PAGE_END);
    }

    private JPanel getFormattedMessagePanel(){
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel,BoxLayout.Y_AXIS));
        labelsPanel.setBackground(AppThemeColor.TRANSPARENT);

        itemLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_IMPORTANT,TextAlignment.CENTER,17f,parsedMessage.get("itemName"));
        labelsPanel.add(itemLabel);
        String curCount = parsedMessage.get("curCount");
        String currency = parsedMessage.get("currency");
        if(curCount != null && currency != null) {
            JLabel priceLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP, 17f, curCount);
            JLabel currencyLabel;
            if (supportedIcons.contains(currency)){
                currencyLabel = componentsFactory.getIconLabel("currency/" + currency + ".png", 28);
            } else {
                currencyLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP, 17f, currency);
            }
            labelsPanel.add(priceLabel);
            labelsPanel.add(currencyLabel);
        }

        String offer = parsedMessage.get("offer");
        if(offer != null) {
            JLabel offerLabel = componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP, 16f, offer);
            labelsPanel.add(offerLabel);
            if (offer.length() > 1) {
                Dimension rectSize = new Dimension();
                rectSize.setSize(350, 130);
                this.setMaximumSize(rectSize);
                this.setMinimumSize(rectSize);
                this.setPreferredSize(rectSize);
            }
        }
        return labelsPanel;
    }
    public void viewed(){
        newLabel.setText("");
        MessagePanel.this.revalidate();
        MessagePanel.this.repaint();
    }
    private JPanel getWhisperPanel(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppThemeColor.TRANSPARENT);

        JLabel whisperLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_NICKNAME, TextAlignment.LEFTOP,15f,whisper + ":");
        Border border = whisperLabel.getBorder();
        whisperLabel.setBorder(new CompoundBorder(border,new EmptyBorder(0,5,0,0)));
//        whisperLabel.setHorizontalAlignment(SwingConstants.CENTER);
        whisperLabel.setVerticalAlignment(SwingConstants.CENTER);

        topPanel.add(whisperLabel,BorderLayout.CENTER);

        JPanel interactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interactionPanel.setBackground(AppThemeColor.TRANSPARENT);
        JButton inviteButton = componentsFactory.getIconButton("app/invite.png", 12);
        inviteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new ChatCommandEvent("/invite " + whisper));
                Timer timer = new Timer(500,null);
                timer.addActionListener(event -> {
                    EventRouter.fireEvent(new CopyToClipboardEvent(itemLabel.getText()));
                    timer.stop();
                });
                timer.start();
            }
        });
        JButton kickButton = componentsFactory.getIconButton("app/kick.png", 12);
        kickButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new ChatCommandEvent("/kick " + whisper));
            }
        });
        JButton tradeButton = componentsFactory.getIconButton("app/trade.png",12);
        JButton openChatButton = componentsFactory.getIconButton("app/openChat.png",12);
        openChatButton.setToolTipText("Open chat");
        openChatButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                EventRouter.fireEvent(new OpenChatEvent(whisper));
            }
        });
        JButton hideButton = componentsFactory.getIconButton("app/close.png",12);
        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

            }
        });
        interactionPanel.add(inviteButton);
        interactionPanel.add(kickButton);
        interactionPanel.add(tradeButton);
        interactionPanel.add(openChatButton);
        interactionPanel.add(hideButton);

        topPanel.add(interactionPanel,BorderLayout.LINE_END);
        return topPanel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(0,0,getWidth(),getHeight());
    }
}
