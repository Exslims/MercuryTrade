package com.mercury.platform.ui.components.panel.settings.page;

import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.HotKeyType;
import com.mercury.platform.shared.config.descriptor.NotificationDescriptor;
import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.entity.message.FlowDirections;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class NotificationSettingsPagePanel extends SettingsPagePanel {
    private PlainConfigurationService<NotificationDescriptor> notificationService;
    private KeyValueConfigurationService<String,HotKeyDescriptor> hotKeyService;
    private NotificationDescriptor generalSnapshot;
    private Map<String, HotKeyDescriptor> hotKeySnapshot;
    private JPanel buttonsTable;

    @Override
    public void createUI() {
        super.createUI();
        this.notificationService = Configuration.get().notificationConfiguration();
        this.hotKeyService = Configuration.get().hotKeysConfiguration();
        this.generalSnapshot = CloneHelper.cloneObject(notificationService.get());
        this.hotKeySnapshot = CloneHelper.cloneObject(hotKeyService.getMap());

        JPanel inPanel = this.adrComponentsFactory.getCounterPanel(this.getIncomingPanel(), "Incoming notification:", AppThemeColor.ADR_BG,true);
        inPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        JPanel outPanel = this.adrComponentsFactory.getCounterPanel(this.getOutgoingPanel(), "Outgoing notification:", AppThemeColor.ADR_BG,true);
        outPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        this.container.add(this.componentsFactory.wrapToSlide(inPanel));
//        this.container.add(this.componentsFactory.wrapToSlide(outPanel));
    }

    @Override
    public void onSave() {
        this.notificationService.set(CloneHelper.cloneObject(this.generalSnapshot));
        this.hotKeyService.set(CloneHelper.cloneObject(this.hotKeySnapshot));
        MercuryStoreCore.buttonsChangedSubject.onNext(true);
    }

    @Override
    public void restore() {
        this.generalSnapshot = CloneHelper.cloneObject(notificationService.get());
        this.hotKeySnapshot = CloneHelper.cloneObject(hotKeyService.getMap());
        this.removeAll();
        this.createUI();
    }

    private JPanel getIncomingPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.ADR_BG);
        JCheckBox enabled = this.componentsFactory.getCheckBox(this.generalSnapshot.isNotificationEnable());
        enabled.addActionListener(action -> {
            this.generalSnapshot.setNotificationEnable(enabled.isSelected());
        });
        JCheckBox dismiss = this.componentsFactory.getCheckBox(this.generalSnapshot.isDismissAfterKick());
        dismiss.addActionListener(action -> {
            this.generalSnapshot.setDismissAfterKick(dismiss.isSelected());
        });
        JCheckBox showLeague = this.componentsFactory.getCheckBox(this.generalSnapshot.isShowLeague());
        showLeague.addActionListener(action -> {
            this.generalSnapshot.setShowLeague(showLeague.isSelected());
        });
        JComboBox flowDirectionPicker = componentsFactory.getComboBox(new String[]{"Upwards", "Downwards"});
        flowDirectionPicker.addActionListener(e -> {
            switch ((String)flowDirectionPicker.getSelectedItem()){
                case "Upwards":{
                    this.generalSnapshot.setFlowDirections(FlowDirections.UPWARDS);
                    break;
                }
                case "Downwards":{
                    this.generalSnapshot.setFlowDirections(FlowDirections.DOWNWARDS);
                    break;
                }
            }
        });
        JSlider limitSlider = componentsFactory.getSlider(2, 20, this.generalSnapshot.getLimitCount(),AppThemeColor.ADR_BG);
        limitSlider.addChangeListener(e -> {
            this.generalSnapshot.setLimitCount(limitSlider.getValue());
        });
        JSlider unfoldSlider = componentsFactory.getSlider(0, 20, this.generalSnapshot.getUnfoldCount(),AppThemeColor.ADR_BG);
        unfoldSlider.addChangeListener(e -> {
            this.generalSnapshot.setUnfoldCount(unfoldSlider.getValue());
        });
        flowDirectionPicker.setSelectedIndex(this.generalSnapshot.getFlowDirections().ordinal());
        propertiesPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR,16));
        propertiesPanel.add(enabled);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Close panel on kick:", FontStyle.REGULAR,16));
        propertiesPanel.add(dismiss);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Show league:", FontStyle.REGULAR,16));
        propertiesPanel.add(showLeague);
//        propertiesPanel.add(this.componentsFactory.getTextLabel("Flow direction:", FontStyle.REGULAR,16));
//        propertiesPanel.add(flowDirectionPicker);
//        propertiesPanel.add(this.componentsFactory.getTextLabel("Pre-group limit:", FontStyle.REGULAR,16));
//        propertiesPanel.add(limitSlider);
//        propertiesPanel.add(this.componentsFactory.getTextLabel("Unfold by default:", FontStyle.REGULAR,16));
//        propertiesPanel.add(unfoldSlider);
        root.add(propertiesPanel,BorderLayout.PAGE_START);
        root.add(this.componentsFactory.wrapToSlide(this.getResponseButtonsPanel(),AppThemeColor.ADR_BG),BorderLayout.CENTER);
//        root.add(this.componentsFactory.wrapToSlide(this.getInNotificationHotKeysPanel(),AppThemeColor.ADR_BG),BorderLayout.PAGE_END);
        return root;
    }
    private JPanel getResponseButtonsPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(4,4), AppThemeColor.SETTINGS_BG);
        this.buttonsTable = this.componentsFactory.getJPanel(new GridLayout(0, 1, 4, 4),AppThemeColor.SETTINGS_BG);
        buttonsTable.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));

        JPanel headerPanel = this.componentsFactory.getJPanel(new BorderLayout(),AppThemeColor.SETTINGS_BG);

        JLabel titleLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Label");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(120,26));
        JLabel valueLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"Response text");
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel hotKeyLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"");
        hotKeyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hotKeyLabel.setPreferredSize(new Dimension(130,20));

        JLabel closeLabel = componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT, null,15f,"");
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel,BorderLayout.LINE_START);
        headerPanel.add(valueLabel,BorderLayout.CENTER);

        JPanel miscPanel = this.componentsFactory.getJPanel(new BorderLayout(),AppThemeColor.SETTINGS_BG);
        miscPanel.add(hotKeyLabel,BorderLayout.CENTER);
        miscPanel.add(closeLabel,BorderLayout.LINE_END);
        headerPanel.add(miscPanel,BorderLayout.LINE_END);

        this.buttonsTable.add(headerPanel);

        this.generalSnapshot.getButtons().forEach(it -> {
            this.buttonsTable.add(this.getResponseRow(it));
        });
        root.add(this.buttonsTable,BorderLayout.CENTER);
        JButton addButton = this.componentsFactory.getIconButton("app/add_button.png", 24, AppThemeColor.HEADER, "Add button");
        addButton.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
        addButton.addActionListener(action -> {
            ResponseButtonDescriptor descriptor = new ResponseButtonDescriptor();
            int size = this.generalSnapshot.getButtons().size();
            descriptor.setId(++size);
            this.generalSnapshot.getButtons().add(descriptor);
            this.buttonsTable.add(this.getResponseRow(descriptor));
            MercuryStoreUI.settingsRepaintSubject.onNext(true);
            MercuryStoreUI.settingsPackSubject.onNext(true);
        });
        root.add(addButton,BorderLayout.PAGE_END);
        return root;
    }
    private JPanel getResponseRow(ResponseButtonDescriptor descriptor){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(4,4), AppThemeColor.SETTINGS_BG);
        JTextField titleField = this.componentsFactory.getTextField(descriptor.getTitle(), FontStyle.REGULAR, 15f);
        titleField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                descriptor.setTitle(titleField.getText());
                System.out.println(".qwe");
            }
        });
        titleField.setPreferredSize(new Dimension(120,26));
        JTextField responseField = this.componentsFactory.getTextField(descriptor.getResponseText(), FontStyle.REGULAR, 15f);
        responseField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                descriptor.setResponseText(responseField.getText());
            }
        });
        root.add(titleField,BorderLayout.LINE_START);
        root.add(responseField,BorderLayout.CENTER);

        JPanel miscPanel = this.componentsFactory.getJPanel(new BorderLayout(4, 4),AppThemeColor.SETTINGS_BG);
        JCheckBox checkBox = this.componentsFactory.getCheckBox(descriptor.isClose(),"Close notification panel after click?");
        checkBox.addActionListener(action -> {
            descriptor.setClose(checkBox.isSelected());
        });
        miscPanel.add(checkBox, BorderLayout.LINE_START);
//        miscPanel.add(new HotKeyPanel(descriptor.getHotKeyDescriptor()),BorderLayout.CENTER);

        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 17, AppThemeColor.SETTINGS_BG, "Remove button");
        removeButton.addActionListener(action -> {
            this.buttonsTable.remove(root);
            this.generalSnapshot.getButtons().remove(descriptor);
            MercuryStoreUI.settingsPackSubject.onNext(true);
            MercuryStoreUI.settingsRepaintSubject.onNext(true);
        });
        miscPanel.add(removeButton,BorderLayout.LINE_END);

        root.add(miscPanel,BorderLayout.LINE_END);
        return root;
    }
    private JPanel getInNotificationHotKeysPanel(){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(0, 4, 4, 4),AppThemeColor.SETTINGS_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.hotKeySnapshot.forEach((key, value) -> {
            if(HotKeyType.contains(key)) {
                JLabel iconLabel = this.componentsFactory.getIconLabel(HotKeyType.valueOf(key).getIconPath(), 18);
                iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
                root.add(iconLabel);
                root.add(this.componentsFactory.wrapToSlide(new HotKeyPanel(value),AppThemeColor.SETTINGS_BG,1,4,1,1));
            }
        });
        return root;
    }
    private JPanel getOutgoingPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.ADR_BG);
        JCheckBox enabled = this.componentsFactory.getCheckBox(this.generalSnapshot.isNotificationEnable());
        enabled.addActionListener(action -> {
            this.generalSnapshot.setNotificationEnable(enabled.isSelected());
        });
        propertiesPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR,16));
        propertiesPanel.add(enabled);
        root.add(propertiesPanel,BorderLayout.PAGE_START);
        return root;
    }

    private class HotKeyPanel extends JPanel {
        private HotKeyDescriptor descriptor;
        private boolean hotKeyAllowed;
        private ComponentsFactory componentsFactory = new ComponentsFactory();
        public HotKeyPanel(HotKeyDescriptor descriptor) {
            super(new BorderLayout());
            this.descriptor = descriptor;
            this.setPreferredSize(new Dimension(130,26));

            JButton button = this.componentsFactory.getBorderedButton(this.descriptor.getTitle());
            button.setFont(this.componentsFactory.getFont(FontStyle.BOLD, 18f));
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)) {
                        button.setBackground(AppThemeColor.ADR_BG);
                        button.setText("Press any key");
                        hotKeyAllowed = true;
                        button.removeMouseListener(this);
                    }
                }
            };
            button.addMouseListener(mouseAdapter);
            MercuryStoreCore.hotKeySubject.subscribe(hotKey -> {
                if (hotKeyAllowed) {
                    button.setBackground(AppThemeColor.BUTTON);
                    if (hotKey.getVirtualKeyCode() == 27) {
                        this.descriptor.setTitle("...");
                        this.descriptor.setVirtualKeyCode(0);
                        this.descriptor.setMenuPressed(false);
                        this.descriptor.setShiftPressed(false);
                        this.descriptor.setControlPressed(false);
                    } else {
                        this.descriptor.setTitle(hotKey.getTitle());
                        this.descriptor.setVirtualKeyCode(hotKey.getVirtualKeyCode());
                        this.descriptor.setMenuPressed(hotKey.isMenuPressed());
                        this.descriptor.setShiftPressed(hotKey.isShiftPressed());
                        this.descriptor.setControlPressed(hotKey.isControlPressed());
                        this.descriptor.setKeyChar(hotKey.getKeyChar());
                    }
                    button.setText(hotKey.getTitle());
                    hotKeyAllowed = false;
                    button.addMouseListener(mouseAdapter);
                }
            });
            this.add(button,BorderLayout.CENTER);
        }
    }
}
