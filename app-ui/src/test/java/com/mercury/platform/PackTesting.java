package com.mercury.platform;

import com.mercury.platform.ui.misc.AppThemeColor;
import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PackTesting {
    private static void createAndShowGUI() {
        System.setProperty("sun.java2d.noddraw","true");
        final JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400,400));
        frame.setMaximumSize(new Dimension(400,400));
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        AWTUtilities.setWindowOpaque(frame,false);
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));
        JButton button = new JButton("test");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
                panel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
                frame.add(panel);
                frame.pack();
            }
        });
        frame.add(button);
        frame.setVisible(true);
        frame.pack();
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
