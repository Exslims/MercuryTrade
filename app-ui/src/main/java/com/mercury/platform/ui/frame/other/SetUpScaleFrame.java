package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.data.ScaleData;
import com.mercury.platform.ui.misc.event.SaveScaleEvent;
import com.mercury.platform.ui.misc.event.ScaleChangeEvent;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SetUpScaleFrame extends OverlaidFrame {
    private ScaleData scaleData;
    public SetUpScaleFrame() {
        super("MercuryTrade");
        scaleData = new ScaleData();
    }

    @Override
    protected void initialize() {
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));

        JPanel rootPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(6,6,0,6));

        JPanel header = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        header.add(componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP,18f,"Scale settings"));

        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.add(getScaleSettingsPanel(),BorderLayout.CENTER);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton cancel = componentsFactory.getBorderedButton("Cancel");
        cancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 3)
        ));
        cancel.setBackground(AppThemeColor.FRAME);

        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FramesManager.INSTANCE.disableScale();
            }
        });
        cancel.setPreferredSize(new Dimension(100, 26));

        JButton save = componentsFactory.getBorderedButton("Save");
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FramesManager.INSTANCE.disableScale();
                EventRouter.UI.fireEvent(new SaveScaleEvent(scaleData));
            }
        });
        save.setPreferredSize(new Dimension(100, 26));

        miscPanel.add(cancel);
        miscPanel.add(save);
        rootPanel.add(root,BorderLayout.CENTER);
        this.add(header,BorderLayout.PAGE_START);
        this.add(rootPanel,BorderLayout.CENTER);
        this.add(miscPanel,BorderLayout.PAGE_END);
//        this.setPreferredSize(new Dimension(450,160));
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/4-this.getSize().height/2);
    }

    private JPanel getScaleSettingsPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(3,2));
        Dimension elementsSize = new Dimension(150, 30);

        JLabel notificationLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Notification panel: ");
        JLabel notificationValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "10");
        notificationValue.setBorder(null);
        JSlider notificationSlider = componentsFactory.getSlider(9,15,10);
        scaleData.setNotificationScale(10/10f);
        notificationSlider.addChangeListener((event)-> repaint());
        notificationSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(notificationSlider.getValue() != prevValue){
                    prevValue = notificationSlider.getValue();
                    notificationValue.setText(String.valueOf(notificationSlider.getValue()));
                    scaleData.setNotificationScale(notificationSlider.getValue()/10f);
                    EventRouter.UI.fireEvent(new ScaleChangeEvent.NotificationScaleChangeEvent(notificationSlider.getValue()/10f));
                    repaint();
                }
            }
        });
        notificationSlider.setPreferredSize(elementsSize);

        JLabel taskBarLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Task panel: ");
        JLabel taskBarValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "10");
        taskBarValue.setBorder(null);
        JSlider taskBarSlider = componentsFactory.getSlider(9,15,10);
        taskBarSlider.addChangeListener((event)-> repaint());
        taskBarSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(taskBarSlider.getValue() != prevValue){
                    prevValue = taskBarSlider.getValue();
                    taskBarValue.setText(String.valueOf(taskBarSlider.getValue()));
                    EventRouter.UI.fireEvent(new ScaleChangeEvent.TaskBarScaleChangeEvent(taskBarSlider.getValue()/10f));
                    repaint();
                }
            }
        });
        taskBarSlider.setPreferredSize(elementsSize);

        JLabel itemInfoLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Item cell panel: ");
        JLabel itemInfoValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "10");
        itemInfoValue.setBorder(null);
        JSlider itemInfoSlider = componentsFactory.getSlider(9,15,10);
        itemInfoSlider.addChangeListener((event)-> repaint());
        itemInfoSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;
            @Override
            public void mouseReleased(MouseEvent e) {
                if(itemInfoSlider.getValue() != prevValue){
                    prevValue = itemInfoSlider.getValue();
                    itemInfoValue.setText(String.valueOf(itemInfoSlider.getValue()));
                    EventRouter.UI.fireEvent(new ScaleChangeEvent.ItemPanelScaleChangeEvent(itemInfoSlider.getValue()/10f));
                    repaint();
                }
            }
        });
        itemInfoSlider.setPreferredSize(elementsSize);

        root.add(wrapLabel(notificationLabel));
        root.add(wrapSliderComponents(notificationSlider,notificationValue));
        root.add(wrapLabel(taskBarLabel));
        root.add(wrapSliderComponents(taskBarSlider,taskBarValue));
        root.add(wrapLabel(itemInfoLabel));
        root.add(wrapSliderComponents(itemInfoSlider,itemInfoValue));
        return root;
    }
    private JPanel wrapSliderComponents(JSlider slider, JLabel sliderLabel){
        JPanel panel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(null);
        panel.add(slider);
        JPanel valuePanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        valuePanel.setBorder(null);
        sliderLabel.setBorder(null);
        valuePanel.add(sliderLabel);
        panel.add(valuePanel,0);
        panel.setBorder(BorderFactory.createEmptyBorder(-2,0,-2,0));
        return panel;
    }
    private JPanel wrapLabel(JLabel label){
        JPanel panel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(label);
        return panel;
    }
    @Override
    public void initHandlers() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
