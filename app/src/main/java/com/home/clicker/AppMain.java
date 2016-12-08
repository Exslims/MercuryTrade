package com.home.clicker;

import javax.swing.*;

public class AppMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WindowFrame frame = new WindowFrame();
            }
        });
    }
}
