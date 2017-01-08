package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 06.01.2017.
 */
public class TimerFrame extends OverlaidFrame {
    private Timer timeAgo;
    private JLabel timeLabel;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private int mapCount = 0;
    protected TimerFrame() {
        super("MT-Timer");
        createUI();
        this.setVisible(false);
        prevState = FrameStates.HIDE;
        disableHideEffect();
        this.pack();
    }

    private void createUI() {
        JPanel root = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JButton play = componentsFactory.getIconButton("app/timer-play.png", 18);
        play.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                getNewTimer().start();
            }
        });
        JButton pause = componentsFactory.getIconButton("app/timer-pause.png", 18);
        pause.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(timeAgo != null){
                    timeAgo.stop();
                }
            }
        });
        JButton stop = componentsFactory.getIconButton("app/timer-stop.png", 18);
        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(timeAgo != null){
                    timeAgo.stop();
                    seconds = 0;
                    minutes = 0;
                    hours = 0;
                }
            }
        });
        JButton reset = componentsFactory.getIconButton("app/timer-reset.png", 18);
        reset.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(timeAgo != null){
                    timeAgo.stop();
                }
                timeLabel.setText("00:00:00");
                seconds = 0;
                minutes = 0;
                hours = 0;
                TimerFrame.this.repaint();
            }
        });
        root.add(getTimePanel());
        root.add(play);
        root.add(pause);
        root.add(stop);
        root.add(reset);
        this.add(root,BorderLayout.CENTER);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel mapCountLabel = componentsFactory.getTextLabel("Map count: 0");
        JButton plus = componentsFactory.getIconButton("app/invite.png", 14);
        plus.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mapCount++;
                mapCountLabel.setText("Map count: " + mapCount);
                TimerFrame.this.repaint();
            }
        });
        JButton minus = componentsFactory.getIconButton("app/kick.png", 14);
        minus.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(mapCount > 0) {
                    mapCount--;
                    mapCountLabel.setText("Map count: " + mapCount);
                    TimerFrame.this.repaint();
                }
            }
        });
        miscPanel.add(mapCountLabel);
        miscPanel.add(plus);
        miscPanel.add(minus);
        this.add(miscPanel,BorderLayout.PAGE_END);
    }
    private Timer getNewTimer(){
        timeAgo = new Timer(1000, e -> {
            String labelText = "";
            seconds++;
            if (seconds > 59) {
                minutes++;
                seconds = 0;
                if (minutes > 59) {
                    hours++;
                    minutes = 0;
                }
            }
            String secLabel = ((seconds/10.0) >= 1f)?String.valueOf(seconds) : "0" + seconds;
            String minLabel = ((minutes/10.0) >= 1f)?String.valueOf(minutes) : "0" + minutes;
            String hLabel = ((hours/10.0) >= 1f)?String.valueOf(hours) : "0" + hours;
            if (minutes == 0 && hours == 0) {
                labelText = "00:00:" + secLabel;
            } else if (minutes > 0) {
                labelText = "00:" + minLabel + ":" + secLabel;
            } else if (hours > 0) {
                labelText = hLabel + ":" + minLabel + ":" + secLabel;
            }
            timeLabel.setText(labelText);
            TimerFrame.this.repaint();
        });
        return timeAgo;
    }

    private JPanel getTimePanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(AppThemeColor.TRANSPARENT);
        timeLabel = componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MISC, TextAlignment.CENTER, 18, "00:00:00");
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0,-2,0,0),
                BorderFactory.createLineBorder(AppThemeColor.BUTTON, 1)
        );
        panel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BUTTON, 1));
        panel.setMinimumSize(new Dimension(100,34));
        panel.setPreferredSize(new Dimension(100,34));
        panel.add(timeLabel);
        return panel;
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Timer";
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
