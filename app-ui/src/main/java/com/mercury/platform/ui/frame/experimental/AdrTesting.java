package com.mercury.platform.ui.frame.experimental;

import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import sun.swing.SwingUtilities2;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Random;

public class AdrTesting extends JPanel {
    protected final JProgressBar progress1 = new JProgressBar() {
        @Override
        public void updateUI() {
            super.updateUI();
            setUI(new ProgressCircleUI());
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
    };
    protected final JProgressBar progress2 = new JProgressBar() {
        @Override
        public void updateUI() {
            super.updateUI();
            setUI(new ProgressCircleUI());
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
    };

    public AdrTesting() {
        super(new BorderLayout());
        progress2.setStringPainted(true);
        progress1.setBackground(AppThemeColor.TRANSPARENT);
        progress1.setBorder(null);
        progress2.setFont(new ComponentsFactory().getFont(FontStyle.BOLD, 82));
        progress2.setForeground(AppThemeColor.TEXT_DEFAULT);

        JSlider slider = new JSlider();
        slider.putClientProperty("Slider.paintThumbArrowShape", Boolean.TRUE);
        progress1.setModel(slider.getModel());

        JButton button = new JButton("start");
        button.addActionListener(e -> {
            JButton b = (JButton) e.getSource();
            b.setEnabled(false);
            SwingWorker<String, Void> worker = new Task() {
                @Override
                public void done() {
                    if (b.isDisplayable()) {
                        b.setEnabled(true);
                    }
                }
            };
            worker.addPropertyChangeListener(new ProgressListener(progress2));
            worker.execute();
        });

        JPanel p = new JPanel(new GridLayout(1, 2));
        p.setBackground(AppThemeColor.TRANSPARENT);
        p.add(progress1);
        p.add(progress2);

        add(slider, BorderLayout.NORTH);
        add(p);
        add(button, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(320, 240));
    }

    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new AdrTesting());
        frame.setUndecorated(true);
        frame.setBackground(AppThemeColor.FRAME);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class ProgressCircleUI extends BasicProgressBarUI {
    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        int v = Math.max(d.width, d.height);
        d.setSize(v, v);
        return d;
    }

    @Override
    protected void paintString(Graphics g, int x, int y, int width, int height, int amountFull, Insets b) {
        Graphics2D g2 = (Graphics2D) g;
        String progressString = String.valueOf(progressBar.getValue());
        g2.setFont(progressBar.getFont());
        Point renderLocation = getStringPlacement(g2, progressString,
                x, y, width, height);
        Rectangle oldClip = g2.getClipBounds();
        g2.setColor(getSelectionForeground());
        SwingUtilities2.drawString(progressBar, g2, progressString,
                renderLocation.x, renderLocation.y);
        g2.setColor(getSelectionForeground());
        g2.clipRect(width, y, amountFull, height);
        SwingUtilities2.drawString(progressBar, g2, progressString,
                renderLocation.x, renderLocation.y);
        g2.setClip(oldClip);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        //public void paintDeterminate(Graphics g, JComponent c) {
        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth = progressBar.getWidth() - b.right - b.left;
        int barRectHeight = progressBar.getHeight() - b.top - b.bottom;
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double degree = 360 * progressBar.getPercentComplete();
        double sz = Math.max(barRectWidth, barRectHeight);
        Shape outer = new Rectangle2D.Double(0, 0, sz, sz);
        Shape sector = new Arc2D.Double(-sz, -sz, sz * 3, sz * 3, 90 - degree, degree, Arc2D.PIE);

        Area foreground = new Area(sector);
        Area background = new Area(outer);

        foreground.intersect(background);

        g2.setPaint(new Color(0x505050));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.fill(background);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        try {
            BufferedImage read = ImageIO.read(getClass().getClassLoader().getResource("app/adr/vessel_vinktar.png"));
            g2.drawImage(read, 0, 0, (int) sz, (int) sz, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.fill(foreground);

        g2.dispose();

//        // Deal with possible text painting
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
        }
    }
}

class Task extends SwingWorker<String, Void> {
    private final Random rnd = new Random();

    @Override
    public String doInBackground() {
        int current = 0;
        int lengthOfTask = 200;
        while (current <= lengthOfTask && !isCancelled()) {
            try { // dummy task
                Thread.sleep(rnd.nextInt(50) + 1);
            } catch (InterruptedException ex) {
                return "Interrupted";
            }
            setProgress(100 * current / lengthOfTask);
            current++;
        }
        return "Done";
    }
}

class ProgressListener implements PropertyChangeListener {
    private final JProgressBar progressBar;

    protected ProgressListener(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setValue(0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String strPropertyName = e.getPropertyName();
        if ("progress".equals(strPropertyName)) {
            progressBar.setIndeterminate(false);
            int progress = (Integer) e.getNewValue();
            progressBar.setValue(progress);
        }
    }
}
