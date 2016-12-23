package com.home.clicker.ui.components.panel;

import com.home.clicker.shared.HasEventHandlers;
import com.home.clicker.shared.events.EventRouter;
import com.home.clicker.shared.events.custom.RepaintEvent;
import com.home.clicker.ui.components.ComponentsFactory;
import com.home.clicker.ui.components.interfaces.HasOpacity;
import com.home.clicker.ui.misc.AppThemeColor;
import com.home.clicker.shared.PoeShortCastSettings;
import org.pushingpixels.trident.Timeline;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Константин on 16.12.2016.
 */
public abstract class TransparencyContainerPanel extends JPanel implements HasEventHandlers, HasOpacity{
    private static final int HIDE_TIME_MS = 200;

    private int opacity;
    private Timeline hideTimeLine;
    private Timeline showTimeLine;

    protected ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;

    protected JScrollPane scroll;
    protected JPanel container;
    protected JPanel headButtonsPanel;
    protected Timer hideTimer;
    protected JButton title;
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


        title = componentsFactory.getButton(PoeShortCastSettings.APP_VERSION);
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

        JButton hideButton = componentsFactory.getIconButton("app/close.png",15);
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


////        initTimeLines();
//        this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
////                if(hideTimeLine != null){
////                    hideTimeLine.end();
////                }
////                showTimeLine = new Timeline(TransparencyContainerPanel.this);
////                showTimeLine.addPropertyToInterpolate("opacity",opacity,100);
////                showTimeLine.setDuration(HIDE_TIME_MS);
////                showTimeLine.play();
//                System.out.println("mouse entered");
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
////                if(showTimeLine != null){
////                    showTimeLine.end();
////                }
////                hideTimeLine = new Timeline(TransparencyContainerPanel.this);
////                hideTimeLine.addPropertyToInterpolate("opacity",opacity,0);
////                hideTimeLine.setDuration(HIDE_TIME_MS);
////                hideTimeLine.play();
//                System.out.println("mouse exited");
//            }
//        });

//        this.setBorder(BorderFactory.createLineBorder(Color.blue,6));
    }
//    private void initTimeLines(){
//        showTimeLine = new Timeline(TransparencyContainerPanel.this);
//        showTimeLine.addPropertyToInterpolate("opacity",opacity,100);
//        showTimeLine.setDuration(HIDE_TIME_MS);
//
//        hideTimeLine = new Timeline(TransparencyContainerPanel.this);
//        hideTimeLine.addPropertyToInterpolate("opacity",opacity,0);
//        hideTimeLine.setDuration(HIDE_TIME_MS);
//    }

    public abstract void initHandlers();

    private Timer getHideTimer(){
        Timer timer = new Timer(7000,null);
        timer.addActionListener(e -> {
            TransparencyContainerPanel.this.setVisible(false);
            timer.stop();
        });
        return timer;
    }

    @Override
    public void setOpacity(int percent) {
        this.opacity = percent;
        this.setBackground(new Color(AppThemeColor.BUTTON.getRed(),
                AppThemeColor.BUTTON.getGreen(), AppThemeColor.BUTTON.getBlue(), percent));
        EventRouter.fireEvent(new RepaintEvent());
    }

    protected void repaintPanel(){
        TransparencyContainerPanel.this.revalidate();
        TransparencyContainerPanel.this.repaint();
    }
}
