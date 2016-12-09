package com.home.clicker;

import com.home.clicker.utils.CachedFilesUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Константин on 09.12.2016.
 */
public class FileChooser extends JFrame {
    private String gamePath = "";
    private JFileChooser fileChooser = new JFileChooser();
    private JTextField textField = new JTextField();
    public FileChooser() {
        JButton openButton = new JButton("Select");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(FileChooser.this);
                if(returnVal == JFileChooser.APPROVE_OPTION){
                    gamePath = fileChooser.getSelectedFile().getPath();
                    textField.setText(gamePath);
                }
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CachedFilesUtils.setGamePath(gamePath);
                new PrivateMessageManager();
                FileChooser.this.setVisible(false);
            }
        });
        JPanel topPanel = new JPanel();
        topPanel.add(textField);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);
        add(topPanel);
        add(buttonPanel);

        setSize(300,100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
}
