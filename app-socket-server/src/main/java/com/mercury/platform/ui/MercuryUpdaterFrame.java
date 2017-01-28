package com.mercury.platform.ui;

import com.mercury.platform.config.MercuryServerConfig;
import com.mercury.platform.server.bus.UpdaterServerAsyncEventBus;
import com.mercury.platform.server.bus.handlers.ClientConnectedEventHandler;
import com.mercury.platform.server.core.UpdaterServer;
import com.mercury.platform.server.main.listeners.ShutdownServerButtonListener;
import com.mercury.platform.server.main.listeners.StartServerButtonListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Created by Frost on 25.01.2017.
 */
public class MercuryUpdaterFrame extends JFrame {

    private static final Logger LOGGER = LogManager.getLogger(MercuryUpdaterFrame.class);

    public static final Dimension DEFAULT_FRAME_SIZE = new Dimension(350, 200);

    private JLabel onlineCount;
    private JLabel updateCount;
    private UpdaterServer server;

    public MercuryUpdaterFrame(){
        this.initServer();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setPreferredSize(DEFAULT_FRAME_SIZE);
        this.add(getTopPanel(),BorderLayout.PAGE_START);
        this.add(getLabelsPanel(),BorderLayout.CENTER);
        this.add(getBottomPanel(),BorderLayout.PAGE_END);
        this.pack();
    }

    private void initServer() {
        MercuryServerConfig serverConfig = MercuryServerConfig.getInstance();

        UpdaterServerAsyncEventBus asyncEventBus = UpdaterServerAsyncEventBus.getInstance();

        asyncEventBus.register((ClientConnectedEventHandler) event ->
                LOGGER.info("Client connected, IP = {}" , event.getIpAddress())
        );

        this.server = new UpdaterServer(serverConfig.getPort());
    }

    private JPanel getTopPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField jarPathField = new JTextField();
        jarPathField.setEditable(false);
        jarPathField.setPreferredSize(new Dimension(220,24));


        FileDialog dialog = new FileDialog(this,"Choose jar", FileDialog.LOAD);
        dialog.setDirectory("C:\\");

        JButton pickJarButton = new JButton("Open");
        pickJarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.setVisible(true);

                new File(dialog.getDirectory() + dialog.getFile());
                jarPathField.setText(dialog.getDirectory() + dialog.getFile());
            }
        });

        panel.add(jarPathField);
        panel.add(pickJarButton);
        return panel;
    }
    private JPanel getLabelsPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        JPanel onlinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel onlineLabel = new JLabel("Online: ");
        //
        onlineCount = new JLabel("0");

        onlinePanel.add(onlineLabel);
        onlinePanel.add(onlineCount);

        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel updateLabel = new JLabel("Update count: ");
        //
        updateCount = new JLabel("0");

        updatePanel.add(updateLabel);
        updatePanel.add(updateCount);

        panel.add(onlinePanel);
        panel.add(updatePanel);
        return panel;
    }
    private JPanel getBottomPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton startUpdate = new JButton("Start");
        startUpdate.addMouseListener(new StartServerButtonListener(server));
        JButton shutdownUpdate = new JButton("Shut down");
        shutdownUpdate.addMouseListener(new ShutdownServerButtonListener(server));

        panel.add(startUpdate);
        panel.add(shutdownUpdate);
        return panel;
    }

}
