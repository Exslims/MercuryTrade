package com.home.clicker;

import com.home.clicker.utils.CachedFilesUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;

/**
 * Exslims
 * 07.12.2016
 */
public class WindowFrame extends JFrame {
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

        if(CachedFilesUtils.getGamePath().equals("")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setVisible(true);
        }else {
            new PrivateMessageManager(this);
        }

    }

    public class FileChooser extends JFrame{
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
                    new PrivateMessageManager(WindowFrame.this);
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
}
