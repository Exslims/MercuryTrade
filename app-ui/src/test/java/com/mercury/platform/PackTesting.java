package com.mercury.platform;

import com.mercury.platform.core.User32;
import com.sun.jna.Native;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 23.12.2016.
 */
public class PackTesting {
    private static User32 user32 = User32.INSTANCE;
    private static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World123123213213");
        frame.getContentPane().add(label);
        frame.setSize(new Dimension(400,400));

        frame.setLocationRelativeTo(null);
        frame.setFocusable(false);
        frame.setFocusableWindowState(false);
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        //Display the window.
        JButton button = new JButton("test");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (frame.isFocusableWindow()) {
                    frame.setVisible(false);
                    frame.setFocusableWindowState(false);
                    setForegroundWindow("Path of Exile");
                } else {
                    frame.setFocusableWindowState(true);
                }
            }
        });
        frame.add(button);
        frame.setVisible(true);
        frame.pack();
    }
    public static void setForegroundWindow(final String titleName){
        user32.EnumWindows((hWnd, arg1) -> {
            byte[] windowText = new byte[512];
            user32.GetWindowTextA(hWnd, windowText, 512);
            String wText = Native.toString(windowText);

            if (wText.isEmpty()) {
                return true;
            }
            if (wText.equals(titleName)) {
                user32.SetForegroundWindow(hWnd);
                return false;
            }
            return true;
        }, null);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
