package com.home.clicker.ui.components;

import com.home.clicker.shared.HasEventHandlers;
import com.home.clicker.ui.components.fields.ExButton;
import com.home.clicker.ui.misc.AppThemeColor;
import com.home.clicker.shared.PoeShortCastSettings;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Константин on 16.12.2016.
 */
public abstract class TransparencyContainerPanel extends JPanel implements HasEventHandlers{
    protected JScrollPane scroll;
    protected JPanel container;
    protected JPanel headButtonsPanel;
    protected Timer hideTimer;
    protected ExButton title;
    protected int x;
    protected int y;
    protected Dimension size;
    public TransparencyContainerPanel(Dimension size) {
        super(new BorderLayout(),true);
        this.size = size;
        init();
        initHandlers();
    }
    protected void init(){
        this.setPreferredSize(new Dimension(size.width,size.height));
        this.setSize(new Dimension(size.width,size.height));
        this.setBackground(AppThemeColor.TRANSPARENT); //todo
        this.setBorder(null);
        container = new JPanel();
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.setBackground(AppThemeColor.TRANSPARENT);
        container.setBorder(null);

        scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.setBackground(AppThemeColor.TRANSPARENT);
        scroll.setPreferredSize(this.getPreferredSize());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                container.repaint();
            }
        });
        JScrollBar verticalScrollBar = scroll.getVerticalScrollBar();
        verticalScrollBar.setUI(new MessagesContainerPanel.ExScrollBarUI());
        verticalScrollBar.setPreferredSize(new Dimension(2, container.getHeight()));
        verticalScrollBar.setBackground(AppThemeColor.TRANSPARENT);
        verticalScrollBar.setUnitIncrement(2);
        verticalScrollBar.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(AppThemeColor.TRANSPARENT);


        title = new ExButton(PoeShortCastSettings.APP_VERSION);
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });
        title.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                e.translatePoint(TransparencyContainerPanel.this.getLocation().x - x,TransparencyContainerPanel.this.getLocation().y - y);
                TransparencyContainerPanel.this.setLocation(e.getX(),e.getY());
            }
        });

        BufferedImage buttonIcon = null;
        try {
            buttonIcon = ImageIO.read(getClass().getClassLoader().getResource("close.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage icon = Scalr.resize(buttonIcon, 15);
        ExButton hideButton = new ExButton(new ImageIcon(icon));

        hideButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TransparencyContainerPanel.this.setVisible(false);
            }
        });

        headButtonsPanel = new JPanel(new BorderLayout());
        headButtonsPanel.setBackground(AppThemeColor.TRANSPARENT);
        headButtonsPanel.setBorder(null);
        headButtonsPanel.add(hideButton, BorderLayout.LINE_END);

        titlePanel.add(headButtonsPanel,BorderLayout.LINE_END);

        titlePanel.add(title,BorderLayout.CENTER);
        add(titlePanel, BorderLayout.PAGE_START);
        this.setVisible(false);
        this.hideTimer = getHideTimer();
    }

    public abstract void initHandlers();

    private Timer getHideTimer(){
        Timer timer = new Timer(7000,null);
        timer.addActionListener(e -> {
            TransparencyContainerPanel.this.setVisible(false);
            timer.stop();
        });
        return timer;
    }
    protected void repaintPanel(){
        TransparencyContainerPanel.this.revalidate();
        TransparencyContainerPanel.this.repaint();
    }
}
