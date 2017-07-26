package com.mercury.platform;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Константин on 26.07.2017.
 */
public class Test2 {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                go();
            }
        });
    }

    public static void go() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Loading...");
        JProgressBar jpb = new JProgressBar();
        jpb.setIndeterminate(true);
        panel.add(label);
        panel.add(jpb);
        frame.add(panel);
        frame.pack();
        frame.setSize(200,90);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new Task_StringUpdate(label).execute();
    }

    static class Task_StringUpdate extends SwingWorker<Void, String> {

        JLabel jlabel;
        public Task_StringUpdate(JLabel jlabel) {
            this.jlabel = jlabel;
        }

        @Override
        protected void process(List<String> chunks) {
            jlabel.setText(chunks.get(chunks.size()-1)); // The last value in this array is all we care about.
            System.out.println(chunks.get(chunks.size()-1));
        }

        @Override
        protected Void doInBackground() throws Exception {

            publish("Loading Step 1...");
            Thread.sleep(1000);
            publish("Loading Step 2...");
            Thread.sleep(1000);
            publish("Loading Step 3...");
            Thread.sleep(1000);
            publish("Loading Step 4...");
            Thread.sleep(1000);

            return null;
        }

        @Override
        protected void done() {
            try {
                get();
                JOptionPane.showMessageDialog(jlabel.getParent(), "Success", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
