package com.mercury.platform.ui.frame.movable.container;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.ItemMessage;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.message.MessagePanel;
import com.mercury.platform.ui.components.panel.message.MessagePanelController;
import com.mercury.platform.ui.components.panel.message.NotificationMessageController;
import com.mercury.platform.ui.components.panel.message.MessagePanelStyle;
import com.mercury.platform.ui.frame.movable.MovableComponentFrame;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.frame.setup.location.LocationState;
import com.mercury.platform.ui.frame.setup.scale.ScaleState;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import com.mercury.platform.ui.misc.data.ScaleData;
import com.mercury.platform.ui.misc.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IncMessageFrame extends MovableComponentFrame implements MessagesContainer {
    private Map<Message, MessagePanel> currentMessages;
    private boolean wasVisible;
    private FlowDirections flowDirections;
    private FlowDirections pikerDirection;
    private boolean expanded = false;
    private JPanel buffer;
    private JSlider limitSlider;
    private int limitMsgCount;
    private JSlider expandSlider;
    private int expandedMsgCount;
    private int currentExpandedMsgCount;

    private ExpandAllFrame expandAllFrame;

    private boolean dnd = false;
    public IncMessageFrame(){
        super("MercuryTrade");

        currentMessages = new HashMap<>();
        processSEResize = false;
        flowDirections = FlowDirections.valueOf(configManager.getFlowDirection());
        pikerDirection = FlowDirections.valueOf(configManager.getFlowDirection());
        limitMsgCount = configManager.getLimitMsgCount();
        expandedMsgCount = configManager.getExpandedMsgCount();
        currentExpandedMsgCount = 0;
        expandAllFrame = new ExpandAllFrame();
        buffer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buffer.setMinimumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        buffer.setBackground(AppThemeColor.TRANSPARENT);
    }

    @Override
    protected void initialize() {
        super.initialize();
        createUI();
    }

    @Override
    public void createUI() {
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.getRootPane().setBorder(null);
        if(currentMessages.size() > 0){
            currentMessages.forEach((message, panel) -> this.mainContainer.add(panel));
            this.setVisible(true);
        }
        if(flowDirections.equals(FlowDirections.UPWARDS)){
            changeDirectionTo(FlowDirections.UPWARDS);
            locationWasChanged = true;
            changeLocation();
        }
        expandAllFrame.init();
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BoxLayout(mainContainer,BoxLayout.Y_AXIS);
    }

    @Override
    public void initHandlers() {
        EventRouter.CORE.registerHandler(DndModeEvent.class, event -> {
            this.dnd = ((DndModeEvent)event).isDnd();
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
        EventRouter.CORE.registerHandler(NewWhispersEvent.class, event -> {
            SwingUtilities.invokeLater(()-> {
                addMessage(((NewWhispersEvent)event).getMessage());
            });
        });
        EventRouter.UI.registerHandler(CloseMessagePanelEvent.class, event -> {
            Message message = ((CloseMessagePanelEvent) event).getMessage();
            MessagePanel panel = currentMessages.get(message);
            if(panel.isExpanded()){
                currentExpandedMsgCount--;
            }
            this.remove(panel);
            currentMessages.remove(message);
            switch (flowDirections){
                case DOWNWARDS:{
                    if(mainContainer.getComponentCount() == 0){
                        this.setVisible(false);
                    }else if(mainContainer.getComponentCount() == limitMsgCount){
                        mainContainer.getComponent((limitMsgCount - 1)).setVisible(true);
                        expandAllFrame.decMessageCount();
                        expandAllFrame.setVisible(false);
                        expanded = false;
                    }else if(mainContainer.getComponentCount() > limitMsgCount) {
                        mainContainer.getComponent((limitMsgCount - 1)).setVisible(true);
                        expandAllFrame.decMessageCount();
                    }
                    break;
                }
                case UPWARDS:{
                    if(mainContainer.getComponentCount() == 1){
                        this.setVisible(false);
                    }else if(mainContainer.getComponentCount() == (limitMsgCount + 1)){
                        mainContainer.getComponent(mainContainer.getComponentCount() - limitMsgCount).setVisible(true);
                        expandAllFrame.decMessageCount();
                        expandAllFrame.setVisible(false);
                        expanded = false;
                    }else if(mainContainer.getComponentCount() > (limitMsgCount + 1)) {
                        mainContainer.getComponent(mainContainer.getComponentCount() - limitMsgCount).setVisible(true);
                        expandAllFrame.decMessageCount();
                    }
                    break;
                }
            }
            this.pack();
            setUpExpandButton();
        });
        EventRouter.UI.registerHandler(ExpandMessageEvent.class, event -> onExpandMessage());
        EventRouter.UI.registerHandler(CollapseMessageEvent.class, event -> onCollapseMessage());
        EventRouter.UI.registerHandler(RepaintEvent.RepaintMessageFrame.class, event -> {
            IncMessageFrame.this.revalidate();
            IncMessageFrame.this.repaint();
        });
    }

    private void addMessage(Message message){
        MessagePanel messagePanel;
        MessagePanelStyle style = flowDirections.equals(FlowDirections.DOWNWARDS)?
                MessagePanelStyle.DOWNWARDS_SMALL: MessagePanelStyle.UPWARDS_SMALL;
        messagePanel = new MessagePanel(
                message,
                style,
                new NotificationMessageController(message),
                this.componentsFactory);

        if (!dnd && !this.isVisible() && AppStarter.APP_STATUS == FrameStates.SHOW) {
            this.setVisible(true);
        } else {
            prevState = FrameStates.SHOW;
        }
        if (flowDirections.equals(FlowDirections.UPWARDS)) {
            mainContainer.add(messagePanel, 1);
        } else {
            mainContainer.add(messagePanel);
        }
        currentMessages.put(message,messagePanel);
        this.pack();
        if (currentExpandedMsgCount < expandedMsgCount) {
            messagePanel.expand();
        }
        switch (flowDirections) {
            case DOWNWARDS: {
                if (mainContainer.getComponentCount() > limitMsgCount && !expanded) {
                    messagePanel.setVisible(false);
                }
                if (mainContainer.getComponentCount() > limitMsgCount) {
                    setUpExpandButton();
                    expandAllFrame.incMessageCount();
                }
                break;
            }
            case UPWARDS: {
                if (mainContainer.getComponentCount() > (limitMsgCount + 1) && !expanded) {
                    messagePanel.setVisible(false);
                }
                if (mainContainer.getComponentCount() > (limitMsgCount + 1)) {
                    setUpExpandButton();
                    expandAllFrame.incMessageCount();
                }
                break;
            }
        }
    }

    private void setUpExpandButton(){
        if(!inScaleSettings && !inMoveMode && !dnd) {
            switch (flowDirections) {
                case DOWNWARDS: {
                    if(mainContainer.getComponentCount() >= (limitMsgCount + 1)) {
                        if (mainContainer.getComponentCount() > limitMsgCount) {
                            Component[] components = mainContainer.getComponents();
                            int height = 0;
                            for (int i = 0; i < limitMsgCount; i++) {
                                height += components[i].getPreferredSize().height;
                            }
                            expandAllFrame.setMinimumSize(new Dimension((int)(20 * componentsFactory.getScale()), height));
                            expandAllFrame.setLocation(new Point(this.getLocation().x - expandAllFrame.getPreferredSize().width,
                                    this.getLocation().y));
                            expandAllFrame.pack();
                        }
                        expandAllFrame.changeArrowDirection();
                        expandAllFrame.setVisible(true);
                    }
                    break;
                }
                case UPWARDS: {
                    if(mainContainer.getComponentCount() >= (limitMsgCount + 2)) {
                        if (mainContainer.getComponentCount() > (limitMsgCount + 1)) {
                            Component[] components = mainContainer.getComponents();
                            int height = 0;
                            for (int i = components.length - 1; i > components.length - (limitMsgCount + 1); i--) {
                                height += components[i].getPreferredSize().height;
                            }
                            Point location = mainContainer.getComponent(components.length - limitMsgCount).getLocationOnScreen();
                            expandAllFrame.setMinimumSize(new Dimension((int)(20 * componentsFactory.getScale()), height));
                            expandAllFrame.setLocation(new Point(this.getLocation().x - expandAllFrame.getPreferredSize().width,
                                    location.y));
                            expandAllFrame.pack();
                        }
                        expandAllFrame.changeArrowDirection();
                        expandAllFrame.setVisible(true);
                    }
                    break;
                }
            }
        }
    }

    @Override
    protected JPanel getPanelForPINSettings() {
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBackground(AppThemeColor.FRAME);
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER,18f,"Notification panel"));

        JPanel outer = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel growPanel = componentsFactory.getTransparentPanel(new GridBagLayout());
        GridBagConstraints titleColumn = new GridBagConstraints();
        titleColumn.gridy = 0;
        titleColumn.gridx = 0;
        titleColumn.fill = GridBagConstraints.HORIZONTAL;
        titleColumn.weightx = 0.2f;
        titleColumn.insets = new Insets(-6,0,0,6);
        GridBagConstraints fieldColumn = new GridBagConstraints();
        fieldColumn.gridy = 0;
        fieldColumn.gridx = 1;
        fieldColumn.fill = GridBagConstraints.HORIZONTAL;
        fieldColumn.weightx = 0.7f;
        fieldColumn.insets = new Insets(-6,0,0,6);

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
            repaint();
        });
        flowDirectionPicker.setSelectedIndex(flowDirections.ordinal());
        growPanel.add(componentsFactory.getTextLabel("Flow direction:"),titleColumn);
        growPanel.add(flowDirectionPicker,fieldColumn);
        titleColumn.gridy = 1;
        fieldColumn.gridy = 1;
        growPanel.add(componentsFactory.getTextLabel("Pre-group limit:"),titleColumn);
        JPanel limitPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel limitCount = componentsFactory.getTextLabel(String.valueOf(limitMsgCount));
        JPanel limitCountPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        limitCountPanel.add(limitCount);
        limitCountPanel.setPreferredSize(new Dimension(14,30));

        limitSlider = componentsFactory.getSlider(2, 20, limitMsgCount);
        limitSlider.addChangeListener(e -> {
            limitCount.setText(String.valueOf(limitSlider.getValue()));
            repaint();
        });
        limitSlider.setPreferredSize(new Dimension(138,30));
        limitPanel.add(limitCountPanel);
        limitPanel.add(limitSlider);
        growPanel.add(limitPanel,fieldColumn);
        titleColumn.gridy = 2;
        fieldColumn.gridy = 2;
        titleColumn.insets = new Insets(-16,0,0,6);
        fieldColumn.insets = new Insets(-16,0,0,6);
        growPanel.add(componentsFactory.getTextLabel("Unfold by default:"),titleColumn);

        JPanel expandPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel expandCount = componentsFactory.getTextLabel(String.valueOf(expandedMsgCount));
        JPanel expandCountPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        expandCountPanel.add(expandCount);
        expandCountPanel.setPreferredSize(new Dimension(14,30));

        expandSlider = componentsFactory.getSlider(0, 20, expandedMsgCount);
        expandSlider.addChangeListener(e -> {
            expandCount.setText(String.valueOf(expandSlider.getValue()));
            repaint();
        });
        expandSlider.setPreferredSize(new Dimension(138,30));
        expandPanel.add(expandCountPanel);
        expandPanel.add(expandSlider);

        growPanel.add(expandPanel,fieldColumn);
        panel.add(labelPanel);
        panel.add(growPanel);
        outer.add(panel,BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension((int)(200 * componentsFactory.getScale()), (int)(110*componentsFactory.getScale())));
        return panel;
    }

    private void onLimitCountChange(){
        expandAllFrame.resetMessageCount();
        Arrays.stream(mainContainer.getComponents()).forEach(component -> {
            component.setVisible(true);
        });
        switch (flowDirections){
            case DOWNWARDS:{
                Component[] components = mainContainer.getComponents();
                for (int i = 0; i < components.length; i++) {
                    if(i > limitMsgCount - 1){
                        components[i].setVisible(false);
                        expandAllFrame.incMessageCount();
                    }
                }
                if(components.length - 1 < limitMsgCount){
                    expandAllFrame.setVisible(false);
                }
                break;
            }
            case UPWARDS:{
                Component[] components = mainContainer.getComponents();
                for (int i = components.length - 1; i > 0; i--) {
                    if(i < (components.length - limitMsgCount)){
                        components[i].setVisible(false);
                        expandAllFrame.incMessageCount();
                    }
                }
                if(components.length - 2 < limitMsgCount){
                    expandAllFrame.setVisible(false);
                }
                break;
            }
        }
    }

    private void onExpandedCountChange(){
        int expanded = 0;
        switch (flowDirections){
            case DOWNWARDS:{
                Component[] components = mainContainer.getComponents();
                for (Component component :components) {
                    if (expanded < expandedMsgCount) {
                        ((MessagePanel) component).expand();
                        expanded++;
                    } else {
                        ((MessagePanel) component).collapse();
                    }
                }
                break;
            }
            case UPWARDS:{
                Component[] components = mainContainer.getComponents();
                for (int i = components.length - 1; i > 0; i--) {
                    if (expanded < expandedMsgCount) {
                        ((MessagePanel) components[i]).expand();
                        expanded++;
                    } else {
                        ((MessagePanel) components[i]).collapse();
                    }
                }
                break;
            }
        }
        currentExpandedMsgCount = expanded;
    }

    @Override
    public void setOpacity(float opacity) {
        super.setOpacity(opacity);
        expandAllFrame.setOpacity(opacity);
    }
    @Override
    protected void onLock() {
        expandAllFrame.setLocationState(LocationState.DEFAULT);
        if(!this.flowDirections.equals(pikerDirection)){
            configManager.setFlowDirection(pikerDirection.toString());
            this.changeDirectionTo(pikerDirection);
            locationWasChanged = true;
        }
        if(limitMsgCount != limitSlider.getValue()) {
            limitMsgCount = limitSlider.getValue();
            configManager.setLimitMsgCount(limitMsgCount);
            this.onLimitCountChange();
        }
        if(expandedMsgCount != expandSlider.getValue()) {
            expandedMsgCount = expandSlider.getValue();
            configManager.setExpandedMsgCount(expandedMsgCount);
            this.onExpandedCountChange();
        }
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
        setUpExpandButton();
        expandAllFrame.setLocationState(LocationState.MOVING);
    }

    @Override
    protected void onScaleLock() {
        if(currentMessages.size() > 0){
            expandAllFrame.setScaleState(ScaleState.DEFAULT);
            this.changeLocation();
            this.setUpExpandButton();
            this.pack();
            this.repaint();
        }
    }

    @Override
    protected void onScaleUnlock() {
        expandAllFrame.setScaleState(ScaleState.ENABLE);
        this.pack();
        this.repaint();
    }

    @Override
    protected void onFrameDragged(Point location) {
        super.onFrameDragged(location);
        expandAllFrame.setLocation(location.x - expandAllFrame.getPreferredSize().width - 2,location.y);
    }

    @Override
    public void onLocationChange(Point location) {
        super.onLocationChange(location);
        if(expandAllFrame.isVisible()) {
            expandAllFrame.setLocation(location.x - expandAllFrame.getPreferredSize().width - 2, location.y);
        }
    }

    private void changeDirectionTo(FlowDirections direction){
        wasVisible = isVisible();
        hideComponent();
        switch (direction) {
            case DOWNWARDS:{
                mainContainer.remove(buffer);
                Component[] components = mainContainer.getComponents();
                for (Component component : components) {
                    ((MessagePanel) component).setStyle(MessagePanelStyle.DOWNWARDS_SMALL);
                    mainContainer.remove(component);
                    mainContainer.add(component, 0);
                }
                break;
            }
            case UPWARDS: {
                mainContainer.add(buffer,0);
                Component[] components = mainContainer.getComponents();
                for (int i = 1; i < components.length; i++) {
                    ((MessagePanel) components[i]).setStyle(MessagePanelStyle.UPWARDS_SMALL);
                    mainContainer.remove(components[i]);
                    mainContainer.add(components[i], 1);
                }
                break;
            }
        }
        if(wasVisible) {
            showComponent();
        }
        this.flowDirections = direction;
    }
    private void changeLocation(){
        wasVisible = isVisible();
        hideComponent();
        switch (flowDirections){
            case DOWNWARDS:{
                this.setLocation(ConfigManager.INSTANCE.getFrameSettings(this.getClass().getSimpleName()).getFrameLocation());
                break;
            }
            case UPWARDS:{
                if(locationWasChanged) {
                    int height = this.getLocation().y;
                    this.setLocation(this.getLocation().x, -1000);
                    this.setMinimumSize(new Dimension(this.getWidth(), height + 1110));
                    this.setMaximumSize(new Dimension(this.getWidth(), height + 1110));
                    locationWasChanged = false;
                }
                break;
            }
        }
        if(wasVisible) {
            showComponent();
        }
    }

    @Override
    protected Point getFrameLocation() {
        if(flowDirections.equals(FlowDirections.UPWARDS)){
            return new Point(this.getLocationOnScreen().x,this.getLocationOnScreen().y + this.getHeight());
        }
        return super.getFrameLocation();
    }

    @Override
    public void onExpandMessage() {
        this.pack();
        if(expandAllFrame.isVisible()) {
            setUpExpandButton();
        }
        currentExpandedMsgCount++;
    }

    @Override
    public void onCollapseMessage() {
        this.pack();
        if(expandAllFrame.isVisible()) {
            setUpExpandButton();
        }
        currentExpandedMsgCount--;
    }

    @Override
    protected void performScaling(ScaleData scaleData) {
        wasVisible = isVisible();
        hideComponent();
        this.componentsFactory.setScale(scaleData.getNotificationScale());
        currentMessages.forEach((message,panel)->{
            panel.setComponentsFactory(this.componentsFactory);
            panel.setStyle(panel.getStyle());
        });
        if(wasVisible) {
            showComponent();
        }
        expandAllFrame.processNewScale();
        changeLocation();
        setUpExpandButton();
        this.pack();
        this.repaint();
    }

    @Override
    protected void registerDirectScaleHandler() {
        EventRouter.UI.registerHandler(ScaleChangeEvent.NotificationScaleChangeEvent.class, event -> {
            changeScale(((ScaleChangeEvent.NotificationScaleChangeEvent)event).getScale());
        });
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
            public void expandMessage() {}
            @Override
            public void collapseMessage() {}
            @Override
            public void reloadMessage(MessagePanel panel1) {}
        };
        MessagePanel messagePanel = new MessagePanel(message, MessagePanelStyle.DOWNWARDS_SMALL, stubController, factory);
        messagePanel.expand();
        panel.add(messagePanel);
        return panel;
    }

    private class ExpandAllFrame extends OverlaidFrame {
        private int messageCount = 0;
        private JLabel msgCountLabel;
        private JButton expandButton;
        private  JPanel labelPanel;
        private Container rootContainer;
        private ExpandAllFrameConstraints prevContraints;
        private boolean wasVisible;

        ExpandAllFrame() {
            super("MercuryTrade");
        }
        @Override
        protected void initialize() {
            this.rootContainer = this.getContentPane();
            this.componentsFactory = IncMessageFrame.this.componentsFactory;
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
                        IncMessageFrame.this.pack();
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
        public void initHandlers() {

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
                    panel.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
                    JLabel infoLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_DEFAULT,TextAlignment.LEFTOP,29f,"?");
                    infoLabel.setOpaque(true);
                    infoLabel.setBackground(AppThemeColor.FRAME);
                    infoLabel.setBorder(BorderFactory.createEmptyBorder(0,3,0,0));
                    infoLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            EventRouter.UI.fireEvent(new ShowTooltipEvent(TooltipConstants.NOTIFICATION_SETTINGS, MouseInfo.getPointerInfo().getLocation()));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            EventRouter.UI.fireEvent(new HideTooltipEvent());
                        }
                    });
                    panel.add(infoLabel,BorderLayout.CENTER);
                    panel.setPreferredSize(new Dimension(this.getPreferredSize().width,IncMessageFrame.this.getPreferredSize().height));
                    this.setLocation(IncMessageFrame.this.getLocation().x - this.getPreferredSize().width - 2, IncMessageFrame.this.getLocation().y);
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
