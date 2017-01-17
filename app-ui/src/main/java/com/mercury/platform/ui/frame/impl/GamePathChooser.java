package com.mercury.platform.ui.frame.impl;
import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.frame.ComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 09.12.2016.
 */
public class GamePathChooser extends ComponentFrame {
    private String gamePath = "";
    private JLabel errorLabel;
    public GamePathChooser() {
        super("Choose game path");
    }

    @Override
    protected void init() {
        super.init();
        this.add(getChooserPanel(),BorderLayout.CENTER);
        this.add(getMiscPanel(),BorderLayout.PAGE_END);

        pack();
        processingHideEvent = false;
    }

    @Override
    protected String getFrameTitle() {
        return "Choose game path";
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    private JPanel getChooserPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppThemeColor.TRANSPARENT);

        errorLabel = componentsFactory.getTextLabel("");
        errorLabel.setForeground(AppThemeColor.TEXT_IMPORTANT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(errorLabel,BorderLayout.CENTER);

        JPanel chooserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chooserPanel.setBackground(AppThemeColor.TRANSPARENT);

        JTextField textField = componentsFactory.getTextField("For example: C:/POE");
        textField.setPreferredSize(new Dimension(500,26));
        textField.setMinimumSize(new Dimension(500,26));
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                GamePathChooser.this.repaint();
            }
        });
        chooserPanel.add(textField);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        JButton selectButton = componentsFactory.getBorderedButton("Select");
        selectButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(GamePathChooser.this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                gamePath = fileChooser.getSelectedFile().getPath();
                textField.setText(gamePath);
                GamePathChooser.this.repaint();
                pack();
            }
        });
        chooserPanel.add(selectButton);
        panel.add(chooserPanel,BorderLayout.PAGE_START);
        return panel;
    }

    private JPanel getMiscPanel() {
        JPanel miscPanel = new JPanel();
        miscPanel.setBackground(AppThemeColor.TRANSPARENT);

        JButton saveButton = componentsFactory.getBorderedButton("Save");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(ConfigManager.INSTANCE.isValidPath(gamePath)) {
                    errorLabel.setText("Success!");
                    errorLabel.setForeground(AppThemeColor.TEXT_SUCCESS);
                    pack();
                    GamePathChooser.this.repaint();
                    Timer timer = new Timer(1000, null);
                    timer.addActionListener(actionEvent -> {
                        ConfigManager.INSTANCE.saveGamePath(gamePath);
                        GamePathChooser.this.setVisible(false);
                        new AppStarter().startApplication();
                        SwingUtilities.invokeLater(TaskBarFrame::new);
                        timer.stop();
                    });
                    timer.start();
                }else {
                    errorLabel.setText("Wrong game path...");
                    pack();
                }
            }
        });
        JButton closeButton = componentsFactory.getBorderedButton("Close");
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
        miscPanel.add(saveButton);
        miscPanel.add(closeButton);
        return miscPanel;
    }
    @Override
    public void initHandlers() {
    }
}
