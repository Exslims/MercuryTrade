package com.home.clicker.ui;

import com.home.clicker.shared.HasEventHandlers;
import com.home.clicker.shared.events.*;
import com.home.clicker.shared.events.custom.*;
import com.home.clicker.ui.components.HistoryContainerPanel;
import com.home.clicker.ui.components.MessagesContainerPanel;
import com.home.clicker.ui.components.SettingsPanel;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Exslims
 * 07.12.2016
 */
public class WindowFrame extends JFrame implements HasEventHandlers {
    private Dimension screenSize;
    private JPopupMenu settingsMenu;
    private MessagesContainerPanel msgContainer;
    private HistoryContainerPanel history;
    private JPanel settingsPanel;

    public WindowFrame() {
        super("PoeShortCast");
        UIManager.getLookAndFeelDefaults().put("Menu.arrowIcon", null);

        setLayout(null);
        getRootPane().setOpaque(false);
        setUndecorated(true);

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setBackground(new Color(0, 0, 0, 0));
        setOpacity(0.9f);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setFocusable(false);

        try {
            initSettingsContextMenu();
            initAppButton();
            initMessagesContainer();
            initHistoryContainer();
            this.settingsPanel = getSettingsPanel();

        } catch (IOException e) {
            e.printStackTrace();
        }
        initHandlers();
    }

    private void initHistoryContainer() {
        history = new HistoryContainerPanel();
        history.setLocation(800,300);
        add(history);
    }

    private void initMessagesContainer(){
        MessagesContainerPanel containerPanel = new MessagesContainerPanel();
        containerPanel.setLocation(300,300);
        containerPanel.setVisible(true);
        add(containerPanel);
        this.msgContainer = containerPanel;
    }

    private void initAppButton() throws IOException {
        BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("chatImage.png"));
        BufferedImage icon = Scalr.resize(buttonIcon, 40);
        JButton button = new JButton(new ImageIcon(icon));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(50,50));
        button.setSize(new Dimension(50,50));
        button.setLocation(30,screenSize.height - 50);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    if (msgContainer.isVisible()) {
                        msgContainer.setVisible(false);
                    } else {
                        msgContainer.setVisible(true);
                    }
                }
            }
        });
        button.setComponentPopupMenu(settingsMenu);
        button.setFocusable(false);

        add(button);
    }
    private void initSettingsContextMenu(){
        settingsMenu = new JPopupMenu("Popup");
        JMenuItem item = new JMenu("settings");
        item.setHorizontalTextPosition(JMenuItem.CENTER);
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                        SettingsFrame settingsFrame = new SettingsFrame();
                        settingsFrame.setVisible(true);
                }
            }
        });

        JMenuItem exit = new JMenu("Exit program");
        exit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        exit.setHorizontalTextPosition(JMenuItem.CENTER);
        exit.setArmed(false);
        settingsMenu.add(item);
        settingsMenu.add(exit);
    }

    private JPanel getSettingsPanel() {
        SettingsPanel sPanel = new SettingsPanel();
        sPanel.setLocation(500,500);
        return sPanel;
    }

    @Override
    public void initHandlers() {
        EventRouter.registerHandler(NewPatchSCEvent.class, event -> {
            JFrame frame = new JFrame("New patch");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JLabel label = new JLabel(((NewPatchSCEvent)event).getPatchTitle());
            frame.getContentPane().add(label);
            frame.pack();
            frame.setVisible(true);
        });

        EventRouter.registerHandler(RepaintEvent.class, event -> {
            WindowFrame.this.revalidate();
            WindowFrame.this.repaint();
        });

        EventRouter.registerHandler(ChangeFrameVisibleEvent.class, new SCEventHandler<ChangeFrameVisibleEvent>() {
            @Override
            public void handle(ChangeFrameVisibleEvent event) {
                switch (event.getStates()){
                    case SHOW:{
                        if(!WindowFrame.this.isShowing()) {
                            WindowFrame.this.setVisible(true);
                        }
                    }
                    break;
                    case HIDE:{
                        if(WindowFrame.this.isShowing()) {
                            WindowFrame.this.setVisible(false);
                        }
                    }
                    break;
                }
            }
        });
    }
//    private void changeState(FrameStates states){
//        switch (states){
//            case SHOW: {
//                chatPanel.setVisible(true);
//                break;
//            }
//            case HIDE:{
//                chatPanel.setVisible(false);
//            }
//            break;
//            case UNDEFINED:{
//                if(chatPanel.isVisible()){
//                    chatPanel.setVisible(false);
//                }else
//                    chatPanel.setVisible(true);
//            }
//        }
//    }
}
