package com.home.clicker.ui.chat;

import com.home.clicker.events.EventRouter;
import com.home.clicker.events.custom.ChatCommandEvent;
import com.home.clicker.events.custom.RemoveChatEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 10.12.2016.
 */
public class ChatTab extends JPanel {
    private String whisper = "default";
    private String initMessage = "default";

    private JTextArea messagesArea;
    private JScrollPane scrollContainer;

    public ChatTab(String whisper) {
        super(new BorderLayout());
        this.whisper = whisper;
        init();
    }

    private void init() {
        add(getChatPanel(), BorderLayout.CENTER);
        add(getActionsPanel(),BorderLayout.LINE_END);
        setBorder(BorderFactory.createLineBorder(Color.blue));
    }

    public ChatTab(String whisper, String initMessage) {
        super(new BorderLayout());
        this.initMessage = initMessage;
        this.whisper = whisper;
        init();
    }

    public void addNewMessage(String message){
        messagesArea.setText(messagesArea.getText() + '\n' + whisper + ":" + message);
        messagesArea.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
    }
    private JPanel getChatPanel(){
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel,BoxLayout.Y_AXIS));

        messagesArea = new JTextArea();
        messagesArea.setText(whisper + ":" + initMessage);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        messagesArea.setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        messagesArea.setMinimumSize((new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE)));
        messagesArea.setEditable(false);


        scrollContainer = new JScrollPane(messagesArea);
        chatPanel.add(scrollContainer);
        scrollContainer.getVerticalScrollBar().setPreferredSize(new Dimension(10,0));
        messagesArea.setCaretPosition(messagesArea.getDocument().getLength());

        JTextField inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE,inputField.getMinimumSize().height));

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    messagesArea.setText(messagesArea.getText() + '\n' + "You:" + inputField.getText());
                    EventRouter.fireEvent(new ChatCommandEvent("@" + whisper + " " + inputField.getText()));
                    getParent().setVisible(true);
                    messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
                    inputField.setText("");
                }
            }
        });
        chatPanel.add(inputField);
        return chatPanel;
    }
    private JPanel getActionsPanel(){
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel,BoxLayout.Y_AXIS));

        JButton inviteButton = new JButton("Invite");
        inviteButton.setMaximumSize(new Dimension(120,inviteButton.getMinimumSize().height));
        inviteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventRouter.fireEvent(new ChatCommandEvent("/invite " + whisper));
            }
        });
        JButton thanksButton = new JButton("Thanks");
        thanksButton.setMaximumSize(new Dimension(120,inviteButton.getMinimumSize().height));
        thanksButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("@" + whisper + " thanks");
                EventRouter.fireEvent(new ChatCommandEvent("@" + whisper + " thanks"));
//                EventRouter.fireEvent(new RemoveChatEvent(whisper));
            }
        });
        JButton nothanksButton = new JButton("No thanks");
        nothanksButton.setMaximumSize(new Dimension(120,inviteButton.getMinimumSize().height));
        nothanksButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("@" + whisper + " no thanks");
                EventRouter.fireEvent(new ChatCommandEvent("@" + whisper + " no thanks"));
//                EventRouter.fireEvent(new RemoveChatEvent(whisper));
            }
        });

        JPanel filler = new JPanel();
        filler.setMaximumSize(new Dimension(120,Integer.MAX_VALUE));
        actionsPanel.add(inviteButton);
        actionsPanel.add(thanksButton);
        actionsPanel.add(nothanksButton);
        actionsPanel.add(filler);

        JButton closeChat = new JButton("Close chat");
        closeChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                EventRouter.fireEvent(new RemoveChatEvent(whisper));
            }
        });
        actionsPanel.add(closeChat);
        return actionsPanel;

    }

    public String getWhisper(){
        return whisper;
    }
}
