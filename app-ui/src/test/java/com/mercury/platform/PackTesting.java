package com.mercury.platform;

import com.mercury.platform.core.User32;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.sun.jna.Native;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 23.12.2016.
 */
public class PackTesting {
    private static void createAndShowGUI() {
        final JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400,400));
        frame.setMaximumSize(new Dimension(400,400));
        frame.setLocationRelativeTo(null);
        frame.setFocusable(false);
        frame.setFocusableWindowState(false);
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));

        JPanel buffer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buffer.setMinimumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        buffer.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_IMPORTANT));

        JButton button = new JButton("test");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JLabel label = new JLabel("Hello World123123213213");
                panel.add(label);
                panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
                panel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
                frame.add(panel,1);
                frame.pack();
            }
        });
        frame.add(buffer);
        frame.add(button);
        frame.setVisible(true);
        frame.pack();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
