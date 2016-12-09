package com.home.clicker;

import com.home.clicker.events.*;
import com.home.clicker.events.custom.ActualWritersChangeEvent;
import com.home.clicker.events.custom.FrameStateChangeEvent;
import com.home.clicker.events.custom.NewPatchSCEvent;
import com.home.clicker.events.custom.SendMessageEvent;
import com.home.clicker.javafx.FrameStates;
import com.home.clicker.utils.CachedFilesUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

/**
 * Exslims
 * 07.12.2016
 */
public class WindowFrame extends JFrame {
    private JPanel nicknamesPanel = new JPanel();
    public WindowFrame() {
        super("ShapedWindow");
        setLayout(new GridBagLayout());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new Ellipse2D.Double(0,0,getWidth(),getHeight()));
            }
        });
        setUndecorated(true);
        setSize(300,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setOpacity(0.7f);
        setVisible(false);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);

        EventRouter.registerHandler(ActualWritersChangeEvent.class, new SCEventHandler<ActualWritersChangeEvent>() {
            public void handle(ActualWritersChangeEvent event) {
                nicknamesPanel.removeAll();
                List<String> writers = event.getWriters();
                for (String writer :writers) {
                    final JButton button = new JButton(writer);
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            String message = button.getText();
                            EventRouter.fireEvent(new SendMessageEvent(message));
                        }
                    });
                    nicknamesPanel.add(button);
                }
                nicknamesPanel.revalidate();
                WindowFrame.this.revalidate();
            }
        });


        EventRouter.registerHandler(FrameStateChangeEvent.class, event -> {
            changeState(((FrameStateChangeEvent)event).getState());
        });
        EventRouter.registerHandler(NewPatchSCEvent.class, new SCEventHandler<NewPatchSCEvent>() {
            @Override
            public void handle(final NewPatchSCEvent event) {
                JFrame frame = new JFrame("New patch");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                JLabel label = new JLabel(event.getPatchTitle());
                frame.getContentPane().add(label);
                frame.pack();
                frame.setVisible(true);
            }
        });

        nicknamesPanel = new JPanel();
        nicknamesPanel.setLayout(new BoxLayout(nicknamesPanel,BoxLayout.Y_AXIS));
        add(nicknamesPanel);

        nicknamesPanel.add(new JButton("TEST"));

    }

    private void changeState(FrameStates states){
        switch (states){
            case SHOW: {
                if(!WindowFrame.this.isShowing()) {
                    WindowFrame.this.setVisible(true);
                }
                break;
            }
            case HIDE:{
                if(WindowFrame.this.isShowing()) {
                    WindowFrame.this.setVisible(false);
                }
            }
        }
    }
    private class PatchNotifierWindow extends Application{

        @Override
        public void start(Stage primaryStage) throws Exception {

        }
    }
}
