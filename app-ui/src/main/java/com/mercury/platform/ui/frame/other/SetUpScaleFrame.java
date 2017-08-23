package com.mercury.platform.ui.frame.other;

import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class SetUpScaleFrame extends AbstractOverlaidFrame {
    private Map<String, Float> scaleData;

    public SetUpScaleFrame() {
        super();
    }

    @Override
    protected void initialize() {
        this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 2),
                BorderFactory.createLineBorder(AppThemeColor.BORDER, 1)));
        this.scaleData = this.scaleConfig.getMap();
    }

    @Override
    public void onViewInit() {

        JPanel rootPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

        JPanel header = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        header.add(componentsFactory.getTextLabel(FontStyle.REGULAR, AppThemeColor.TEXT_DEFAULT, TextAlignment.LEFTOP, 18f, "Scale settings"));

        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);
        root.add(getScaleSettingsPanel(), BorderLayout.CENTER);

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
                MercuryStoreCore.saveConfigSubject.onNext(true);
                MercuryStoreUI.saveScaleSubject.onNext(scaleData);
            }
        });
        save.setPreferredSize(new Dimension(100, 26));

        miscPanel.add(cancel);
        miscPanel.add(save);
        rootPanel.add(root, BorderLayout.CENTER);
        this.add(header, BorderLayout.PAGE_START);
        this.add(rootPanel, BorderLayout.CENTER);
        this.add(miscPanel, BorderLayout.PAGE_END);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 4 - this.getSize().height / 2);
    }

    private JPanel getScaleSettingsPanel() {
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(3, 1));
        JLabel notificationLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Notification panel: ");
        JSlider notificationSlider = componentsFactory.getSlider(9, 15, (int) (scaleData.get("notification") * 10));
        JLabel notificationValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                String.valueOf(notificationSlider.getValue() * 10) + "%");
        notificationValue.setBorder(null);
        notificationSlider.addChangeListener((event) -> repaint());
        notificationSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (notificationSlider.getValue() != prevValue) {
                    prevValue = notificationSlider.getValue();
                    notificationValue.setText(String.valueOf(notificationSlider.getValue() * 10) + "%");
                    scaleData.put("notification", notificationSlider.getValue() / 10f);
                    MercuryStoreUI.notificationScaleSubject.onNext(notificationSlider.getValue() / 10f);
                    repaint();
                }
            }
        });

        JLabel taskBarLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Task panel: ");

        JSlider taskBarSlider = componentsFactory.getSlider(9, 15, (int) (scaleData.get("taskbar") * 10));
        JLabel taskBarValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                String.valueOf(taskBarSlider.getValue() * 10) + "%");
        taskBarValue.setBorder(null);
        taskBarSlider.addChangeListener((event) -> repaint());
        taskBarSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (taskBarSlider.getValue() != prevValue) {
                    prevValue = taskBarSlider.getValue();
                    taskBarValue.setText(String.valueOf(taskBarSlider.getValue() * 10) + "%");
                    scaleData.put("taskbar", taskBarSlider.getValue() / 10f);
                    MercuryStoreUI.taskBarScaleSubject.onNext(taskBarSlider.getValue() / 10f);
                    repaint();
                }
            }
        });

        JLabel itemInfoLabel = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                "Item cell panel: ");
        JSlider itemInfoSlider = componentsFactory.getSlider(9, 15, (int) (scaleData.get("itemcell") * 10));
        JLabel itemInfoValue = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                17f,
                String.valueOf(itemInfoSlider.getValue() * 10) + "%");
        itemInfoValue.setBorder(null);
        itemInfoSlider.addChangeListener((event) -> repaint());
        itemInfoSlider.addMouseListener(new MouseAdapter() {
            private int prevValue = 10;

            @Override
            public void mouseReleased(MouseEvent e) {
                if (itemInfoSlider.getValue() != prevValue) {
                    prevValue = itemInfoSlider.getValue();
                    itemInfoValue.setText(String.valueOf(itemInfoSlider.getValue() * 10) + "%");
                    scaleData.put("itemcell", itemInfoSlider.getValue() / 10f);
                    MercuryStoreUI.itemPanelScaleSubject.onNext(itemInfoSlider.getValue() / 10f);
                    repaint();
                }
            }
        });

        root.add(componentsFactory.getSliderSettingsPanel(notificationLabel, notificationValue, notificationSlider));
        root.add(componentsFactory.getSliderSettingsPanel(taskBarLabel, taskBarValue, taskBarSlider));
        root.add(componentsFactory.getSliderSettingsPanel(itemInfoLabel, itemInfoValue, itemInfoSlider));
        return root;
    }

    @Override
    public void subscribe() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

}
