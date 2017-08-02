package com.mercury.platform.ui.frame.movable.container;

import com.mercury.platform.core.ProdStarter;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.config.Configuration;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationDescriptor;
import com.mercury.platform.shared.entity.message.FlowDirections;
import com.mercury.platform.shared.entity.message.ItemMessage;
import com.mercury.platform.shared.entity.message.Message;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.message.MessagePanel;
import com.mercury.platform.ui.components.panel.message.MessagePanelController;
import com.mercury.platform.ui.components.panel.message.NotificationMessageController;
import com.mercury.platform.ui.components.panel.message.MessagePanelStyle;
import com.mercury.platform.ui.frame.movable.AbstractMovableComponentFrame;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.frame.setup.location.LocationState;
import com.mercury.platform.ui.frame.setup.scale.ScaleState;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.TooltipConstants;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MessageFrame extends AbstractMovableComponentFrame implements MessagesContainer {
    private List<MessagePanel> currentMessages = new ArrayList<>();
    private PlainConfigurationService<NotificationDescriptor> notificationConfig;
    private boolean wasVisible;
    private FlowDirections flowDirections;
    private FlowDirections pikerDirection;
    private boolean expanded = false;
    private JPanel buffer;
    private JSlider limitSlider;
    private int limitMsgCount;
    private JSlider unfoldSlider;
    private int unfoldCount;
    private int currentUnfoldCount;

    private ExpandAllFrame expandAllFrame;

    private boolean dnd = false;
    public MessageFrame(){
        super();
        this.componentsFactory.setScale(this.scaleConfig.get("notification"));
        this.stubComponentsFactory.setScale(this.scaleConfig.get("notification"));
        this.notificationConfig = Configuration.get().notificationConfiguration();
        this.processSEResize = false;
        this.flowDirections = this.notificationConfig.get().getFlowDirections();
        this.pikerDirection = this.notificationConfig.get().getFlowDirections();
        this.limitMsgCount = this.notificationConfig.get().getLimitCount();
        this.unfoldCount = this.notificationConfig.get().getUnfoldCount();
        this.currentUnfoldCount = 0;
        this.expandAllFrame = new ExpandAllFrame();
        this.buffer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.buffer.setMinimumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        this.buffer.setBackground(AppThemeColor.TRANSPARENT);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.createUI();
    }

    @Override
    public void createUI() {
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.getRootPane().setBorder(null);
        if(this.currentMessages.size() > 0){
            this.currentMessages.forEach(panel -> this.mainContainer.add(panel));
            this.setVisible(true);
        }
        if(this.flowDirections.equals(FlowDirections.UPWARDS)){
            this.changeDirectionTo(FlowDirections.UPWARDS);
            this.locationWasChanged = true;
            this.changeLocation();
        }
        this.expandAllFrame.init();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BoxLayout(mainContainer,BoxLayout.Y_AXIS);
    }

    @Override
    public void subscribe() {
        MercuryStoreCore.dndSubject.subscribe(state -> {
            this.dnd = state;
            if(dnd){
                this.setVisible(false);
                expandAllFrame.setVisible(false);
            }else if(flowDirections.equals(FlowDirections.UPWARDS)){
                if(mainContainer.getComponentCount() > 1){
                    this.setVisible(true);
                    setUpExpandButton();
                }
            }else if(mainContainer.getComponentCount() > 0){
                this.setVisible(true);
                setUpExpandButton();
            }
        });
        MercuryStoreCore.messageSubject.subscribe(message -> SwingUtilities.invokeLater(()-> {
            List<MessagePanel> collect = this.currentMessages.stream()
                    .filter(panel -> panel.getMessage().equals(message))
                    .collect(Collectors.toList());
            if(collect.size() == 0) {
                this.addMessage(message);
            }
        }));
        MercuryStoreUI.closeMessage.subscribe(message -> {
            MessagePanel messagePanel = this.currentMessages.stream()
                    .filter(panel -> panel.getMessage().equals(message))
                    .collect(Collectors.toList()).get(0);
            if(messagePanel.isExpanded()){
                this.currentUnfoldCount--;
                if(this.currentUnfoldCount < 0){
                    this.currentUnfoldCount = 0;
                }
            }
            this.remove(messagePanel);
            this.currentMessages.remove(messagePanel);

            if (this.currentMessages.size() == 0) {
                this.setVisible(false);
            } else if (this.currentMessages.size() >= this.limitMsgCount) {
                if(this.currentMessages.size() == this.limitMsgCount) {
                    this.expandAllFrame.setVisible(false);
                }
                this.currentMessages.get(this.limitMsgCount - 1).setVisible(true);
                this.expandAllFrame.decMessageCount();
            }
            this.pack();
            this.setUpExpandButton();
        });
        MercuryStoreUI.expandMessageSubject.subscribe(state -> this.onExpandMessage());
        MercuryStoreUI.collapseMessageSubject.subscribe(state -> this.onCollapseMessage());
    }

    private void addMessage(Message message){
        MessagePanelStyle style = flowDirections.equals(FlowDirections.DOWNWARDS)?
                MessagePanelStyle.DOWNWARDS_SMALL: MessagePanelStyle.UPWARDS_SMALL;
        MessagePanel messagePanel = new MessagePanel(
                message,
                style,
                new NotificationMessageController(message),
                this.componentsFactory);
        if (!dnd && !this.isVisible() && ProdStarter.APP_STATUS == FrameVisibleState.SHOW) {
            this.showComponent();
        } else {
            this.prevState = FrameVisibleState.SHOW;
        }
        if (flowDirections.equals(FlowDirections.UPWARDS)) {
            this.mainContainer.add(messagePanel, 1);
        } else {
            this.mainContainer.add(messagePanel);
        }
        this.currentMessages.add(messagePanel);
        this.pack();
        if (this.currentUnfoldCount < this.unfoldCount) {
            messagePanel.expand();
        }
        if(this.currentMessages.size() > this.limitMsgCount){
            if(!expanded) {
                messagePanel.setVisible(false);
            }
            if(ProdStarter.APP_STATUS == FrameVisibleState.SHOW) {
                this.setUpExpandButton();
            }
            this.expandAllFrame.incMessageCount();
        }
    }

    @Override
    protected void changeVisible(FrameVisibleState state) {
        super.changeVisible(state);
        if(state.equals(FrameVisibleState.SHOW)) {
            this.setUpExpandButton();
        }
    }

    private void setUpExpandButton(){
        if(!inScaleSettings && !inMoveMode && !dnd) {
            switch (flowDirections) {
                case DOWNWARDS: {
                    if(this.mainContainer.getComponentCount() >= (limitMsgCount + 1)) {
                        if (this.mainContainer.getComponentCount() > limitMsgCount) {
                            Component[] components = mainContainer.getComponents();
                            int height = 0;
                            for (int i = 0; i < limitMsgCount; i++) {
                                height += components[i].getPreferredSize().height;
                            }
                            this.expandAllFrame.setMinimumSize(new Dimension((int)(20 * componentsFactory.getScale()), height));
                            this.expandAllFrame.setLocation(new Point(this.getLocation().x - expandAllFrame.getPreferredSize().width,
                                    this.getLocation().y));
                            this.expandAllFrame.pack();
                        }
                        this.expandAllFrame.changeArrowDirection();
                        this.expandAllFrame.setVisible(true);
                    }
                    break;
                }
                case UPWARDS: {
                    if(this.mainContainer.getComponentCount() >= (limitMsgCount + 2)) {
                        if (this.mainContainer.getComponentCount() > (limitMsgCount + 1)) {
                            Component[] components = mainContainer.getComponents();
                            int height = 0;
                            for (int i = components.length - 1; i > components.length - (limitMsgCount + 1); i--) {
                                height += components[i].getPreferredSize().height;
                            }
                            Point location = mainContainer.getComponent(components.length - limitMsgCount).getLocationOnScreen();
                            this.expandAllFrame.setMinimumSize(new Dimension((int)(20 * componentsFactory.getScale()), height));
                            this.expandAllFrame.setLocation(new Point(this.getLocation().x - expandAllFrame.getPreferredSize().width,
                                    location.y));
                            this.expandAllFrame.pack();
                        }
                        this.expandAllFrame.changeArrowDirection();
                        this.expandAllFrame.setVisible(true);
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected JPanel getPanelForPINSettings() {
        JPanel panel = componentsFactory.getTransparentPanel(new GridLayout(4,1));
        panel.setBackground(AppThemeColor.ADR_BG);
        JPanel labelPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        labelPanel.setBackground(AppThemeColor.ADR_BG);
        JLabel headerLabel = componentsFactory.getTextLabel(
                FontStyle.BOLD,
                this.notificationConfig.get().isNotificationEnable()?AppThemeColor.TEXT_MESSAGE:AppThemeColor.TEXT_DISABLE,
                TextAlignment.CENTER,
                18f,
                "Notification panel");
        labelPanel.add(headerLabel,BorderLayout.CENTER);
        JButton enableButton = this.componentsFactory.getBorderedButton(this.notificationConfig.get().isNotificationEnable() ? "Disable" : "Enable");
        enableButton.addActionListener(action -> {
            boolean notificationEnable = this.notificationConfig.get().isNotificationEnable();
            this.notificationConfig.get().setNotificationEnable(!notificationEnable);
            if(this.notificationConfig.get().isNotificationEnable()){
                headerLabel.setForeground(AppThemeColor.TEXT_MESSAGE);
            }else {
                headerLabel.setForeground(AppThemeColor.TEXT_DISABLE);
            }
            enableButton.setText(!notificationEnable ? "Disable" : "Enable");
            MercuryStoreCore.saveConfigSubject.onNext(true);
        });
        enableButton.setFont(this.componentsFactory.getFont(FontStyle.BOLD,18f));
        enableButton.setPreferredSize(new Dimension(100,26));
        labelPanel.add(this.componentsFactory.wrapToSlide(enableButton,AppThemeColor.ADR_BG),BorderLayout.LINE_END);
        panel.add(labelPanel);
        JComboBox flowDirectionPicker = componentsFactory.getComboBox(new String[]{"Upwards", "Downwards"});
        flowDirectionPicker.setSelectedIndex(FlowDirections.valueOf(flowDirections.toString()).ordinal());
        flowDirectionPicker.addActionListener(e -> {
            switch ((String)flowDirectionPicker.getSelectedItem()){
                case "Upwards":{
                    pikerDirection = FlowDirections.UPWARDS;
                    break;
                }
                case "Downwards":{
                    pikerDirection = FlowDirections.DOWNWARDS;
                    break;
                }
            }
        });
        flowDirectionPicker.setSelectedIndex(flowDirections.ordinal());
        panel.add(componentsFactory.getSettingsPanel(
                componentsFactory.getTextLabel("Flow direction:"),flowDirectionPicker));
        JLabel limitCount = componentsFactory.getTextLabel(String.valueOf(limitMsgCount));
        this.limitSlider = componentsFactory.getSlider(2, 20, limitMsgCount,AppThemeColor.ADR_BG);
        this.limitSlider.addChangeListener(e -> {
            limitCount.setText(String.valueOf(limitSlider.getValue()));
        });
        panel.add(componentsFactory.getSliderSettingsPanel(
                componentsFactory.getTextLabel("Pre-group limit:"),
                limitCount,
                limitSlider
                ));
        JLabel unfoldCount = componentsFactory.getTextLabel(String.valueOf(this.unfoldCount));
        this.unfoldSlider = componentsFactory.getSlider(0, 20, this.unfoldCount,AppThemeColor.ADR_BG);
        this.unfoldSlider.addChangeListener(e -> {
            unfoldCount.setText(String.valueOf(unfoldSlider.getValue()));
        });
        panel.add(componentsFactory.getSliderSettingsPanel(
                this.componentsFactory.getTextLabel("Unfold by default:"),
                unfoldCount,
                this.unfoldSlider
        ));
        panel.setPreferredSize(new Dimension((int)(400 * componentsFactory.getScale()), (int)(130*componentsFactory.getScale())));
        this.setMaximumSize(panel.getPreferredSize());
        return panel;
    }

    private void onLimitCountChange(){
        this.expandAllFrame.resetMessageCount();
        Arrays.stream(mainContainer.getComponents())
                .forEach(component -> component.setVisible(true));
        this.currentMessages.stream().skip(this.limitMsgCount).forEach(panel -> {
            panel.setVisible(false);
            this.expandAllFrame.incMessageCount();
        });
        if(this.currentMessages.size() < this.limitMsgCount){
            this.expandAllFrame.setVisible(false);
        }
    }

    private void onExpandedCountChange(){
        this.currentUnfoldCount = 0;
        this.currentMessages.forEach(MessagePanel::collapse);
        this.currentUnfoldCount = 0;
        this.currentMessages.stream().limit(this.unfoldCount).forEach(panel -> {
            panel.expand();
            this.currentUnfoldCount++;
        });
    }

    @Override
    public void setOpacity(float opacity) {
        super.setOpacity(opacity);
        this.expandAllFrame.setOpacity(opacity);
    }
    @Override
    protected void onLock() {
        this.expandAllFrame.setLocationState(LocationState.DEFAULT);
        if(!this.flowDirections.equals(pikerDirection)){
            this.notificationConfig.get().setFlowDirections(this.pikerDirection);
            this.changeDirectionTo(pikerDirection);
            this.locationWasChanged = true;
        }
        if(this.limitMsgCount != this.limitSlider.getValue()) {
            this.limitMsgCount = this.limitSlider.getValue();
            this.notificationConfig.get().setLimitCount(this.limitMsgCount);
            this.onLimitCountChange();
        }
        if(this.unfoldCount != this.unfoldSlider.getValue()) {
            this.unfoldCount = this.unfoldSlider.getValue();
            this.notificationConfig.get().setUnfoldCount(this.unfoldCount);
            this.onExpandedCountChange();
        }
        MercuryStoreCore.saveConfigSubject.onNext(true);
        this.changeLocation();
        super.onLock();
        this.setUpExpandButton();
        if(currentMessages.size() > 0){
            this.setVisible(true);
        }
    }
    @Override
    protected void onUnlock() {
        super.onUnlock();
        this.setUpExpandButton();
        this.expandAllFrame.setLocationState(LocationState.MOVING);
    }

    @Override
    protected void onScaleLock() {
        if(currentMessages.size() > 0){
            this.setVisible(true);
            this.expandAllFrame.setScaleState(ScaleState.DEFAULT);
            this.changeLocation();
            this.setUpExpandButton();
            this.pack();
            this.repaint();
        }
    }

    @Override
    protected void onScaleUnlock() {
        if(currentMessages.size() > 0) {
            this.setVisible(true);
        }
        this.expandAllFrame.setScaleState(ScaleState.ENABLE);
        this.pack();
        this.repaint();
    }

    @Override
    protected void onFrameDragged(Point location) {
        super.onFrameDragged(location);
        this.expandAllFrame.setLocation(location.x - expandAllFrame.getPreferredSize().width - 2,location.y);
    }

    @Override
    public void onLocationChange(Point location) {
        super.onLocationChange(location);
        if(this.expandAllFrame.isVisible()) {
            this.expandAllFrame.setLocation(location.x - expandAllFrame.getPreferredSize().width - 2, location.y);
        }
    }

    private void changeDirectionTo(FlowDirections direction){
        this.wasVisible = isVisible();
        this.hideComponent();
        switch (direction) {
            case DOWNWARDS:{
                this.mainContainer.remove(this.buffer);
                Component[] components = this.mainContainer.getComponents();
                for (Component component : components) {
                    ((MessagePanel) component).setStyle(MessagePanelStyle.DOWNWARDS_SMALL);
                    this.mainContainer.remove(component);
                    this.mainContainer.add(component, 0);
                }
                break;
            }
            case UPWARDS: {
                this.mainContainer.add(buffer,0);
                Component[] components = this.mainContainer.getComponents();
                for (int i = 1; i < components.length; i++) {
                    ((MessagePanel) components[i]).setStyle(MessagePanelStyle.UPWARDS_SMALL);
                    this.mainContainer.remove(components[i]);
                    this.mainContainer.add(components[i], 1);
                }
                break;
            }
        }
        if(this.wasVisible) {
            this.showComponent();
        }
        this.flowDirections = direction;
    }
    private void changeLocation(){
        this.wasVisible = isVisible();
        this.hideComponent();
        switch (flowDirections){
            case DOWNWARDS:{
                this.setLocation(this.framesConfig.get(this.getClass().getSimpleName()).getFrameLocation());
                break;
            }
            case UPWARDS:{
                if(this.locationWasChanged) {
                    int height = this.getLocation().y;
                    this.setLocation(this.getLocation().x, -1000);
                    this.setMinimumSize(new Dimension(this.getWidth(), height + 1000 + (int)(130*componentsFactory.getScale())));
                    this.setMaximumSize(new Dimension(this.getWidth(), height + 1000 + (int)(130*componentsFactory.getScale())));
                    this.locationWasChanged = false;
                }
                break;
            }
        }
        if(this.wasVisible) {
            this.showComponent();
        }
    }

    @Override
    protected Point getFrameLocation() {
        if(this.flowDirections.equals(FlowDirections.UPWARDS)){
            return new Point(this.getLocationOnScreen().x,this.getLocationOnScreen().y + this.getHeight());
        }
        return super.getFrameLocation();
    }

    @Override
    public void onExpandMessage() {
        this.pack();
        if(this.expandAllFrame.isVisible()) {
            this.setUpExpandButton();
        }
        this.currentUnfoldCount++;
    }

    @Override
    public void onCollapseMessage() {
        this.pack();
        if(this.expandAllFrame.isVisible()) {
            this.setUpExpandButton();
        }
        this.currentUnfoldCount--;
    }

    @Override
    protected void performScaling(Map<String,Float> scaleData) {
        this.wasVisible = isVisible();
        this.hideComponent();
        this.componentsFactory.setScale(scaleData.get("notification"));
        this.currentMessages.forEach(panel -> {
            panel.setComponentsFactory(this.componentsFactory);
            panel.setStyle(panel.getStyle());
        });
        if(this.wasVisible) {
            this.showComponent();
        }
        this.expandAllFrame.processNewScale();
        this.changeLocation();
        this.setUpExpandButton();
        this.pack();
        this.repaint();
    }

    @Override
    protected void registerDirectScaleHandler() {
        MercuryStoreUI.notificationScaleSubject.subscribe(this::changeScale);
    }

    @Override
    protected JPanel defaultView(ComponentsFactory factory) {
        ItemMessage message = new ItemMessage();
        message.setWhisperNickname("Example1");
        message.setItemName("Example example example");
        message.setCurrency("chaos");
        message.setCurCount(1000d);
        message.setLeague("Standard");
        message.setOffer("Offer offer offer");

        JPanel panel = factory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(AppThemeColor.FRAME);
        MessagePanelController stubController = new MessagePanelController() {
            @Override
            public void performInvite() {}
            @Override
            public void performKick() {}
            @Override
            public void performOfferTrade() {}
            @Override
            public void performOpenChat() {}
            @Override
            public void performResponse(String responseText) {}
            @Override
            public void performHide() {}
            @Override
            public void showITH() {}
            @Override
            public void reloadMessage(MessagePanel panel1) {}
        };
        MessagePanel messagePanel = new MessagePanel(message, MessagePanelStyle.DOWNWARDS_SMALL, stubController, factory);
        messagePanel.expand();
        panel.add(messagePanel);
        return panel;
    }

    private class ExpandAllFrame extends AbstractOverlaidFrame {
        private int messageCount = 0;
        private JLabel msgCountLabel;
        private JButton expandButton;
        private  JPanel labelPanel;
        private Container rootContainer;
        private ExpandAllFrameConstraints prevContraints;
        private boolean wasVisible;

        ExpandAllFrame() {
            super();
        }
        @Override
        protected void initialize() {
            this.rootContainer = this.getContentPane();
            this.componentsFactory = MessageFrame.this.componentsFactory;
            this.setBackground(AppThemeColor.MSG_HEADER);
            this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(1,1,1,0,AppThemeColor.TRANSPARENT),
                            BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.BORDER)));

            labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
            labelPanel.setBackground(AppThemeColor.MSG_HEADER);
            labelPanel.setPreferredSize(new Dimension((int)(10 * componentsFactory.getScale()),(int)(22 * componentsFactory.getScale())));
            labelPanel.setBorder(BorderFactory.createEmptyBorder(-4,0,0,0));
            msgCountLabel = componentsFactory.getTextLabel("+" + String.valueOf(messageCount));
            String iconPath = (flowDirections.equals(FlowDirections.DOWNWARDS))? "app/collapse-all.png" : "app/expand-all.png";
            expandButton = componentsFactory.getIconButton(iconPath,22,AppThemeColor.MSG_HEADER,"");
            expandButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)){
                        String iconPath;
                        if(!expanded){
                            Arrays.stream(mainContainer.getComponents()).forEach(panel ->{
                                if(!panel.isVisible()){
                                    panel.setVisible(true);
                                }
                            });
                            if(flowDirections.equals(FlowDirections.DOWNWARDS)){
                                iconPath = "app/expand-all.png";
                            }else {
                                iconPath = "app/collapse-all.png";
                            }
                            msgCountLabel.setText("");
                            expanded = true;
                        }else {
                            if(flowDirections.equals(FlowDirections.DOWNWARDS)){
                                iconPath = "app/collapse-all.png";
                            }else {
                                iconPath = "app/expand-all.png";
                            }
                            Component[] components = mainContainer.getComponents();
                            if(flowDirections.equals(FlowDirections.UPWARDS)){
                                for (int i = 1; i < mainContainer.getComponentCount() - limitMsgCount; i++) {
                                    components[i].setVisible(false);
                                }
                            }else {
                                for (int i = 0; i < components.length; i++) {
                                    if(i > (limitMsgCount-1)){
                                        components[i].setVisible(false);
                                    }
                                }
                            }
                            msgCountLabel.setText("+"+String.valueOf(messageCount));
                            expanded = false;
                        }
                        expandButton.setIcon(componentsFactory.getIcon(iconPath,22));
                        MessageFrame.this.pack();
                    }
                }
            });
            expandButton.setAlignmentY(SwingConstants.CENTER);
            labelPanel.add(msgCountLabel);
            if(flowDirections.equals(FlowDirections.DOWNWARDS)){
                rootContainer.add(expandButton,BorderLayout.CENTER);
                rootContainer.add(labelPanel,BorderLayout.PAGE_END);
            }else {
                rootContainer.add(labelPanel, BorderLayout.PAGE_START);
                rootContainer.add(expandButton, BorderLayout.CENTER);
            }
            this.pack();
        }
        void processNewScale(){
            labelPanel.setPreferredSize(new Dimension((int)(10 * componentsFactory.getScale()),(int)(22 * componentsFactory.getScale())));
            msgCountLabel.setFont(componentsFactory.getFont(FontStyle.BOLD,15f));
            changeArrowDirection();
        }

        void incMessageCount(){
            messageCount++;
            if(!expanded) {
                msgCountLabel.setText("+" + String.valueOf(messageCount));
                rootContainer.repaint();
            }

        }
        void decMessageCount(){
            messageCount--;
            if(!expanded) {
                msgCountLabel.setText("+" + String.valueOf(messageCount));
                rootContainer.repaint();
            }
        }
        void resetMessageCount(){
            messageCount = 0;
        }
        void changeArrowDirection(){
            String iconPath = "";
            if(flowDirections.equals(FlowDirections.DOWNWARDS)){
                if(!expanded){
                    iconPath = "app/collapse-all.png";
                }else {
                    iconPath = "app/expand-all.png";
                }
            }else {
                if(!expanded){
                    iconPath = "app/expand-all.png";
                }else {
                    iconPath = "app/collapse-all.png";
                }
            }
            expandButton.setIcon(componentsFactory.getIcon(iconPath,22));
            rootContainer.remove(labelPanel);
            if(pikerDirection.equals(FlowDirections.DOWNWARDS)){
                rootContainer.add(labelPanel,BorderLayout.PAGE_END);
            }else {
                rootContainer.add(labelPanel, BorderLayout.PAGE_START);
            }
            this.pack();
        }
        @Override
        public void subscribe() {

        }

        void setLocationState(LocationState state){
            switch (state){
                case DEFAULT:{
                    toDefaultState();
                    break;
                }
                case MOVING:{
                    prevContraints = new ExpandAllFrameConstraints(
                            this.isVisible(),
                            this.getRootPane().getBorder(),
                            this.getBackground()
                    );
                    this.getRootPane().setBorder(null);
                    this.setBackground(AppThemeColor.FRAME);
                    this.setMinimumSize(null);
                    this.setVisible(true);
                    JPanel panel = componentsFactory.getTransparentPanel(new BorderLayout());
                    panel.setBackground(AppThemeColor.ADR_BG);
                    panel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
                    JLabel infoLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT,TextAlignment.LEFTOP,29f,"?");
                    infoLabel.setOpaque(true);
                    infoLabel.setBackground(AppThemeColor.ADR_BG);
                    infoLabel.setBorder(BorderFactory.createEmptyBorder(0,3,0,0));
                    infoLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            MercuryStoreCore.tooltipSubject.onNext(TooltipConstants.NOTIFICATION_SETTINGS);
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            MercuryStoreCore.tooltipSubject.onNext(null);
                        }
                    });
                    panel.add(infoLabel,BorderLayout.CENTER);
                    panel.setPreferredSize(new Dimension(this.getPreferredSize().width,MessageFrame.this.getPreferredSize().height));
                    this.setLocation(MessageFrame.this.getLocation().x - this.getPreferredSize().width - 2, MessageFrame.this.getLocation().y);
                    this.setContentPane(panel);
                    this.pack();
                    break;
                }
            }
        }
        private void toDefaultState(){
            this.setContentPane(rootContainer);
            this.setVisible(prevContraints.visible);
            this.getRootPane().setBorder(prevContraints.border);
            this.setBackground(prevContraints.bgColor);
            prevContraints = null;
            setUpExpandButton();
            this.pack();
        }

        void setScaleState(ScaleState state){
            switch (state){
                case DEFAULT: {
                    toDefaultState();
                    break;
                }
                case ENABLE:{
                    prevContraints = new ExpandAllFrameConstraints(
                            this.isVisible(),
                            this.getRootPane().getBorder(),
                            this.getBackground()
                    );
                    this.setVisible(false);
                    break;
                }
            }
        }

        @Override
        protected LayoutManager getFrameLayout() {
            return new BorderLayout();
        }

        private class ExpandAllFrameConstraints {
            private boolean visible;
            private Border border;
            private Color bgColor;

            public ExpandAllFrameConstraints(boolean visible, Border border, Color bgColor) {
                this.visible = visible;
                this.border = border;
                this.bgColor = bgColor;
            }
        }
    }
}
