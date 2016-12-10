package com.home.clicker;

import com.home.clicker.events.*;
import com.home.clicker.events.custom.ActualWritersChangeEvent;
import com.home.clicker.events.custom.FrameStateChangeEvent;
import com.home.clicker.events.custom.NewPatchSCEvent;
import com.home.clicker.events.custom.SendMessageEvent;
import com.home.clicker.javafx.FrameStates;
import com.home.clicker.utils.CachedFilesUtils;
import com.pagosoft.plaf.PlafOptions;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.imgscalr.Scalr;
import org.pushingpixels.trident.Timeline;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Exslims
 * 07.12.2016
 */
public class WindowFrame extends JFrame {
    private JTabbedPane chatPanel;
    private int x;
    private int y;
    public WindowFrame() {
        super("ShapedWindow");

        PlafOptions.setAsLookAndFeel();
        PlafOptions.updateAllUIs();

        setLayout(null);
        getRootPane().setOpaque(false);
        setUndecorated(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width,screenSize.height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setBackground(new Color(0,0,0,0));
        setOpacity(0.9f);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);

        try {
            add(createChatButton());
            this.chatPanel = createChatPanel();
            add(chatPanel);

        } catch (IOException e) {
            e.printStackTrace();
        }
        registerUIHandlers();
    }

    private JTabbedPane createChatPanel(){
        JTabbedPane chat = new JTabbedPane();
        chat.setPreferredSize(new Dimension(300,300));
        chat.setSize(new Dimension(300,300));
        chat.setLocation(300,300);
        chat.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        chat.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                e.translatePoint(e.getComponent().getLocation().x - x,e.getComponent().getLocation().y - y);
                chat.setLocation(e.getX(),e.getY());
            }
        });
        chat.setVisible(false);
        return chat;
    }
    private JPanel createChatTab(String whisperName){
        JPanel panel = new JPanel();
        panel.add(new JLabel(whisperName));
        return panel;
    }

    private JButton createChatButton() throws IOException {
        BufferedImage buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("chatImage.png"));
        BufferedImage icon = Scalr.resize(buttonIcon, 40);
        JButton button = new JButton(new ImageIcon(icon));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(50,50));
        button.setSize(new Dimension(50,50));
        button.setLocation(0,680);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("CLICKED");
                if(chatPanel.isVisible()) {
                    changeState(FrameStates.HIDE);
                }else {
                    changeState(FrameStates.SHOW);
                }
            }
        });
        return button;
    }

    private void registerUIHandlers(){
        EventRouter.registerHandler(ActualWritersChangeEvent.class, event -> {
            List<String> writers = ((ActualWritersChangeEvent)event).getWriters();
            for (String writer :writers) {
                JPanel chatTab = createChatTab(writer);
                chatPanel.addTab(writer,chatTab);
//                final JButton button = new JButton(writer);
//                button.addMouseListener(new MouseAdapter() {
//                    @Override
//                    public void mouseClicked(MouseEvent e) {
//                        String message = button.getText();
//                        EventRouter.fireEvent(new SendMessageEvent(message));
//                    }
//                });
//                nicknamesPanel.add(button);
            }
//            nicknamesPanel.revalidate();
//            WindowFrame.this.revalidate();
        });

        EventRouter.registerHandler(FrameStateChangeEvent.class, event -> {
            changeState(((FrameStateChangeEvent)event).getState());
        });
        EventRouter.registerHandler(NewPatchSCEvent.class, event -> {
            JFrame frame = new JFrame("New patch");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JLabel label = new JLabel(((NewPatchSCEvent)event).getPatchTitle());
            frame.getContentPane().add(label);
            frame.pack();
            frame.setVisible(true);
        });
    }

    private void changeState(FrameStates states){
        switch (states){
            case SHOW: {
                chatPanel.setVisible(true);
//                Timeline opacityTimeLine = new Timeline(chatRootPanel);
//                opacityTimeLine.addPropertyToInterpolate("opacity", 0f, 1f);
//                opacityTimeLine.setDuration(300);
//                opacityTimeLine.play();
                break;
            }
            case HIDE:{
                chatPanel.setVisible(false);
//                Timeline opacityTimeLine = new Timeline(chatRootPanel);
//                opacityTimeLine.addPropertyToInterpolate("opacity",1f,0f);
//                opacityTimeLine.setDuration(300);
//                opacityTimeLine.play();
            }
        }
    }
    private class PatchNotifierWindow extends Application{

        @Override
        public void start(Stage primaryStage) throws Exception {

        }
    }
}
