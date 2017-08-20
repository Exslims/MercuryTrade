package com.mercury.platform.ui.components.panel.settings.page;

import com.mercury.platform.shared.CloneHelper;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.KeyValueConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.*;
import com.mercury.platform.shared.entity.message.FlowDirections;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class NotificationSettingsPagePanel extends SettingsPagePanel {
    private PlainConfigurationService<NotificationSettingsDescriptor> notificationService;
    private PlainConfigurationService<HotKeysSettingsDescriptor> hotKeyService;
    private PlainConfigurationService<ScannerDescriptor> scannerService;
    private NotificationSettingsDescriptor generalSnapshot;
    private List<HotKeyPair> incHotKeySnapshot;
    private List<HotKeyPair> outHotKeySnapshot;
    private List<HotKeyPair> scannerHotKeySnapshot;
    private ScannerDescriptor scannerSnapshot;

    @Override
    public void onViewInit() {
        super.onViewInit();
        this.notificationService = Configuration.get().notificationConfiguration();
        this.hotKeyService = Configuration.get().hotKeysConfiguration();
        this.scannerService = Configuration.get().scannerConfiguration();
        this.generalSnapshot = CloneHelper.cloneObject(notificationService.get());
        this.incHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getIncNHotKeysList());
        this.outHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getOutNHotKeysList());
        this.scannerHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getScannerNHotKeysList());
        this.scannerSnapshot = CloneHelper.cloneObject(scannerService.get());
        JPanel inPanel = this.adrComponentsFactory.getCounterPanel(this.getIncomingPanel(), "Incoming notification:", AppThemeColor.ADR_BG,false);
        inPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        JPanel outPanel = this.adrComponentsFactory.getCounterPanel(this.getOutgoingPanel(), "Outgoing notification:", AppThemeColor.ADR_BG,false);
        outPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        JPanel scannerPanel = this.adrComponentsFactory.getCounterPanel(this.getChatScannerPanel(), "Chat scanner notification:", AppThemeColor.ADR_BG,false);
        scannerPanel.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        this.container.add(this.componentsFactory.wrapToSlide(this.getGeneralPanel(),4,4,2,4));
        this.container.add(this.componentsFactory.wrapToSlide(inPanel,2,4,2,4));
        this.container.add(this.componentsFactory.wrapToSlide(outPanel,2,4,2,4));
        this.container.add(this.componentsFactory.wrapToSlide(scannerPanel,2,4,2,4));
    }

    @Override
    public void onSave() {
        this.notificationService.set(CloneHelper.cloneObject(this.generalSnapshot));
        this.hotKeyService.get().setIncNHotKeysList(CloneHelper.cloneObject(this.incHotKeySnapshot));
        this.hotKeyService.get().setOutNHotKeysList(CloneHelper.cloneObject(this.outHotKeySnapshot));
        this.hotKeyService.get().setScannerNHotKeysList(CloneHelper.cloneObject(this.scannerHotKeySnapshot));
        this.scannerService.set(CloneHelper.cloneObject(this.scannerSnapshot));

        //protect
        this.scannerSnapshot = CloneHelper.cloneObject(scannerService.get());

        MercuryStoreCore.buttonsChangedSubject.onNext(true);
    }

    @Override
    public void restore() {
        this.incHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getIncNHotKeysList());
        this.outHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getOutNHotKeysList());
        this.scannerHotKeySnapshot = CloneHelper.cloneObject(hotKeyService.get().getScannerNHotKeysList());
        this.removeAll();
        this.onViewInit();
    }

    private JPanel getGeneralPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.ADR_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
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
        JLabel limitLabel = this.componentsFactory.getTextLabel(String.valueOf(this.generalSnapshot.getLimitCount()), FontStyle.REGULAR, 16);
        limitLabel.setPreferredSize(new Dimension(30,26));
        JSlider limitSlider = componentsFactory.getSlider(2, 20, this.generalSnapshot.getLimitCount(),AppThemeColor.ADR_BG);
        limitSlider.addChangeListener(e -> {
            limitLabel.setText(String.valueOf(limitSlider.getValue()));
            this.generalSnapshot.setLimitCount(limitSlider.getValue());
        });
        flowDirectionPicker.setSelectedIndex(this.generalSnapshot.getFlowDirections().ordinal());
        propertiesPanel.add(this.componentsFactory.getTextLabel("Flow direction:", FontStyle.REGULAR,16));
        propertiesPanel.add(flowDirectionPicker);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Pre-group limit:", FontStyle.REGULAR,16));
        JPanel limitPanel = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        limitPanel.add(limitLabel,BorderLayout.LINE_START);
        limitPanel.add(limitSlider,BorderLayout.CENTER);
        propertiesPanel.add(limitPanel);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Your nickname(for leave option):", FontStyle.REGULAR,16));
        JTextField nickNameField = this.componentsFactory.getTextField(this.generalSnapshot.getPlayerNickname(), FontStyle.DEFAULT, 15f);
        nickNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                generalSnapshot.setPlayerNickname(nickNameField.getText());
            }
        });
        propertiesPanel.add(nickNameField);
        root.add(this.componentsFactory.wrapToSlide(propertiesPanel,AppThemeColor.ADR_BG,2,0,2,2),BorderLayout.PAGE_START);
        return root;
    }

    private JPanel getIncomingPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.ADR_BG);
        JCheckBox enabled = this.componentsFactory.getCheckBox(this.generalSnapshot.isIncNotificationEnable());
        enabled.addActionListener(action -> {
            this.generalSnapshot.setIncNotificationEnable(enabled.isSelected());
        });
        JCheckBox dismiss = this.componentsFactory.getCheckBox(this.generalSnapshot.isDismissAfterKick());
        dismiss.addActionListener(action -> {
            this.generalSnapshot.setDismissAfterKick(dismiss.isSelected());
        });
        JCheckBox showLeague = this.componentsFactory.getCheckBox(this.generalSnapshot.isShowLeague());
        showLeague.addActionListener(action -> {
            this.generalSnapshot.setShowLeague(showLeague.isSelected());
        });
        propertiesPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR,16));
        propertiesPanel.add(enabled);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Close panel on kick:", FontStyle.REGULAR,16));
        propertiesPanel.add(dismiss);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Show league:", FontStyle.REGULAR,16));
        propertiesPanel.add(showLeague);
        root.add(propertiesPanel,BorderLayout.PAGE_START);

        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(this.getResponseButtonsPanel(this.generalSnapshot.getButtons()),AppThemeColor.ADR_BG),"Response buttons:"),BorderLayout.CENTER);
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(this.getInNotificationHotKeysPanel(),AppThemeColor.ADR_BG),"Hotkeys"),BorderLayout.PAGE_END);
        root.setVisible(false);
        return root;
    }
    private JPanel wrapToCounter(JPanel inner, String title){
        JPanel root = this.adrComponentsFactory.getCounterPanel(inner, title, AppThemeColor.ADR_BG,false);
        inner.setVisible(false);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_PANEL_BORDER));
        return this.componentsFactory.wrapToSlide(root,AppThemeColor.ADR_BG);
    }
    private JPanel getResponseButtonsPanel(List<ResponseButtonDescriptor> buttons){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(4,4), AppThemeColor.SETTINGS_BG);
        JPanel buttonsTable = this.componentsFactory.getJPanel(new GridLayout(0, 1, 4, 4),AppThemeColor.SETTINGS_BG);
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

        buttonsTable.add(headerPanel);

        buttons.forEach(it -> {
            buttonsTable.add(this.getResponseRow(it));
        });
        root.add(buttonsTable,BorderLayout.CENTER);
        JButton addButton = this.componentsFactory.getIconButton("app/add_button.png", 22, AppThemeColor.HEADER, "Add button");
        addButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                BorderFactory.createEmptyBorder(3,3,3,3)));
        addButton.addActionListener(action -> {
            ResponseButtonDescriptor descriptor = new ResponseButtonDescriptor();
            int size = buttons.size();
            descriptor.setId(++size);
            buttons.add(descriptor);
            buttonsTable.add(this.getResponseRow(descriptor));
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
        root.add(this.componentsFactory.wrapToSlide(titleField,AppThemeColor.SETTINGS_BG,0,2,2,0),BorderLayout.LINE_START);
        root.add(this.componentsFactory.wrapToSlide(responseField,AppThemeColor.SETTINGS_BG,0,0,2,0),BorderLayout.CENTER);

        JPanel miscPanel = this.componentsFactory.getJPanel(new BorderLayout(4, 4),AppThemeColor.SETTINGS_BG);
        JCheckBox checkBox = this.componentsFactory.getCheckBox(descriptor.isClose(),"Close notification panel after click?");
        checkBox.addActionListener(action -> {
            descriptor.setClose(checkBox.isSelected());
        });
        miscPanel.add(checkBox, BorderLayout.LINE_START);
        miscPanel.add(this.componentsFactory.wrapToSlide(new HotKeyPanel(descriptor.getHotKeyDescriptor()),AppThemeColor.SETTINGS_BG,0,0,2,0),BorderLayout.CENTER);

        JButton removeButton = this.componentsFactory.getIconButton("app/adr/remove_node.png", 17, AppThemeColor.SETTINGS_BG, "Remove button");
        removeButton.addActionListener(action -> {
            root.getParent().remove(root);
            this.generalSnapshot.getButtons().remove(descriptor);
            MercuryStoreUI.settingsPackSubject.onNext(true);
            MercuryStoreUI.settingsRepaintSubject.onNext(true);
        });
        miscPanel.add(removeButton,BorderLayout.LINE_END);

        root.add(miscPanel,BorderLayout.LINE_END);
        return root;
    }
    private JPanel getInNotificationHotKeysPanel(){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.SETTINGS_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.incHotKeySnapshot.forEach(pair -> {
            root.add(this.componentsFactory.getIconLabel(pair.getType().getIconPath(), 18,SwingConstants.CENTER));
            root.add(this.componentsFactory.wrapToSlide(new HotKeyPanel(pair.getDescriptor()),AppThemeColor.SETTINGS_BG,2,4,1,1));

        });
        return root;
    }
    private JPanel getOutNotificationHotKeysPanel(){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.SETTINGS_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.outHotKeySnapshot.forEach(pair -> {
            root.add(this.componentsFactory.getIconLabel(pair.getType().getIconPath(), 18,SwingConstants.CENTER));
            root.add(this.componentsFactory.wrapToSlide(new HotKeyPanel(pair.getDescriptor()),AppThemeColor.SETTINGS_BG,2,4,1,1));
        });
        return root;
    }
    private JPanel getScannerNotificationHotKeysPanel(){
        JPanel root = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.SETTINGS_BG);
        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.ADR_DEFAULT_BORDER));
        this.scannerHotKeySnapshot.forEach(pair-> {
            root.add(this.componentsFactory.getIconLabel(pair.getType().getIconPath(), 18,SwingConstants.CENTER));
            root.add(this.componentsFactory.wrapToSlide(new HotKeyPanel(pair.getDescriptor()),AppThemeColor.SETTINGS_BG,2,4,1,1));

        });
        return root;
    }

    private JPanel getOutgoingPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.ADR_BG);
        JCheckBox enabled = this.componentsFactory.getCheckBox(this.generalSnapshot.isOutNotificationEnable());
        enabled.addActionListener(action -> {
            this.generalSnapshot.setOutNotificationEnable(enabled.isSelected());
        });
        propertiesPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR,16));
        propertiesPanel.add(enabled);
        propertiesPanel.add(this.componentsFactory.getTextLabel("Close panel after leave:", FontStyle.REGULAR,16));
        JCheckBox closeAfterLeave = this.componentsFactory.getCheckBox(this.generalSnapshot.isDismissAfterLeave());
        closeAfterLeave.addActionListener(action -> {
            this.generalSnapshot.setDismissAfterLeave(closeAfterLeave.isSelected());
        });
        propertiesPanel.add(closeAfterLeave);

        root.add(propertiesPanel,BorderLayout.PAGE_START);
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(this.getResponseButtonsPanel(this.generalSnapshot.getOutButtons()),AppThemeColor.ADR_BG),"Response buttons:"),BorderLayout.CENTER);
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(this.getOutNotificationHotKeysPanel(),AppThemeColor.ADR_BG),"Hotkeys:"),BorderLayout.PAGE_END);
        root.setVisible(false);
        return root;
    }
    private JPanel getChatScannerPanel() {
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout(), AppThemeColor.ADR_BG);
        JPanel propertiesPanel = this.componentsFactory.getJPanel(new GridLayout(0, 2, 4, 4),AppThemeColor.ADR_BG);
        JCheckBox enabled = this.componentsFactory.getCheckBox(this.generalSnapshot.isScannerNotificationEnable());
        enabled.addActionListener(action -> {
            this.generalSnapshot.setScannerNotificationEnable(enabled.isSelected());
        });
        propertiesPanel.add(this.componentsFactory.getTextLabel("Enabled:", FontStyle.REGULAR,16));
        propertiesPanel.add(enabled);
        JLabel quickResponseLabel = this.componentsFactory.getIconLabel(HotKeyType.N_QUICK_RESPONSE.getIconPath(), 18);
        quickResponseLabel.setFont(this.componentsFactory.getFont(FontStyle.REGULAR,16));
        quickResponseLabel.setForeground(AppThemeColor.TEXT_DEFAULT);
        quickResponseLabel.setBorder(BorderFactory.createEmptyBorder(0,4,0,0));
        quickResponseLabel.setText("Response message:");
        propertiesPanel.add(quickResponseLabel);
        JTextField quickResponseField = this.componentsFactory.getTextField(this.scannerSnapshot.getResponseMessage(), FontStyle.BOLD, 15f);
        quickResponseField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                scannerSnapshot.setResponseMessage(quickResponseField.getText());
            }
        });
        propertiesPanel.add(this.componentsFactory.wrapToSlide(quickResponseField,AppThemeColor.ADR_BG,0,0,0,4));
        root.add(propertiesPanel,BorderLayout.PAGE_START);
        root.add(this.wrapToCounter(this.componentsFactory.wrapToSlide(this.getScannerNotificationHotKeysPanel(),AppThemeColor.ADR_BG),"Hotkeys"),BorderLayout.CENTER);
        root.setVisible(false);
        return root;
    }
}
