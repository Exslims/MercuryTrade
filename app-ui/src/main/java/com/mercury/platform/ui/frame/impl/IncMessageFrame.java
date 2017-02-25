package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.AppStarter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.*;
import com.mercury.platform.shared.pojo.Message;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.MessagePanel;
import com.mercury.platform.ui.components.panel.misc.MessagePanelStyle;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.frame.OverlaidFrame;
import com.mercury.platform.ui.frame.impl.util.FlowDirections;
import com.mercury.platform.ui.frame.impl.util.TradeMode;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * Created by Константин on 24.12.2016.
 */
public class IncMessageFrame extends MovableComponentFrame implements MessagesContainer {
    private final Logger logger = LogManager.getLogger(IncMessageFrame.class.getSimpleName());
    private TradeMode tradeMode;
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
        super("MT-IncMessagesFrame");
        processSEResize = false;
        tradeMode = TradeMode.valueOf(configManager.getTradeMode());
        flowDirections = FlowDirections.valueOf(configManager.getFlowDirection());
        pikerDirection = FlowDirections.valueOf(configManager.getFlowDirection());
        limitMsgCount = configManager.getLimitMsgCount();
        expandedMsgCount = configManager.getExpandedMsgCount();
        currentExpandedMsgCount = 0;
        expandAllFrame = new ExpandAllFrame();
        buffer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buffer.setMinimumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        buffer.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_IMPORTANT));
        buffer.setBackground(AppThemeColor.TRANSPARENT);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.getRootPane().setBorder(null);
        if(flowDirections.equals(FlowDirections.UPWARDS)){
            changeDirectionTo(FlowDirections.UPWARDS);
            locationWasChanged = true;
            changeLocation();
        }
        expandAllFrame.init();
    }

    private void convertFrameTo(TradeMode mode){
        switch (mode){
            case DEFAULT:{
                if(tradeMode == TradeMode.SUPER){
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        if(flowDirections.equals(FlowDirections.DOWNWARDS)) {
                            ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.DOWNWARDS_SMALL);
                        }else {
                            ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.UPWARDS_SMALL);
                        }
                    }
                }
                break;
            }
            case SUPER:{
                if(tradeMode == TradeMode.DEFAULT){
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.SP_MODE);
                    }
                }
                break;
            }
        }
        this.tradeMode = mode;
        if(mainContainer.getComponentCount() > 0){
            this.pack();
            this.repaint();
        }
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BoxLayout(mainContainer,BoxLayout.Y_AXIS);
    }

    @Override
    public void initHandlers() {
        EventRouter.INSTANCE.registerHandler(ChangedTradeModeEvent.ToSuperTradeModeEvent.class, event -> {
            convertFrameTo(TradeMode.SUPER);
        });
        EventRouter.INSTANCE.registerHandler(ChangedTradeModeEvent.ToDefaultTradeModeEvent.class, event -> {
            convertFrameTo(TradeMode.DEFAULT);
        });
        EventRouter.INSTANCE.registerHandler(DndModeEvent.class, event -> {
            this.dnd = ((DndModeEvent)event).isDnd();
            if(dnd){
                this.setVisible(false);
            }else if(mainContainer.getComponentCount() > 0){
                this.setVisible(true);
            }
        });
        EventRouter.INSTANCE.registerHandler(NewWhispersEvent.class, event -> {
            Message message = ((NewWhispersEvent) event).getMessage();
            MessagePanel messagePanel = null;
            switch (tradeMode) {
                case SUPER: {
                    messagePanel = new MessagePanel(message, this, MessagePanelStyle.SP_MODE);
                    break;
                }
                case DEFAULT: {
                    if(flowDirections.equals(FlowDirections.DOWNWARDS)) {
                        messagePanel = new MessagePanel(message, this, MessagePanelStyle.DOWNWARDS_SMALL);
                    }else {
                        messagePanel = new MessagePanel(message, this, MessagePanelStyle.UPWARDS_SMALL);
                    }
                }
            }
            if (!dnd && !this.isVisible() && AppStarter.APP_STATUS == FrameStates.SHOW) {
                this.setVisible(true);
            } else {
                prevState = FrameStates.SHOW;
            }
            if(flowDirections.equals(FlowDirections.UPWARDS)){
                mainContainer.add(messagePanel, 1);
            }else {
                mainContainer.add(messagePanel);
            }
            this.pack();
            if(currentExpandedMsgCount < expandedMsgCount){
                messagePanel.expand();
                currentExpandedMsgCount++;
            }
            switch (flowDirections){
                case DOWNWARDS:{
                    if(mainContainer.getComponentCount() > limitMsgCount && !expanded){
                        messagePanel.setVisible(false);
                    }
                    if(mainContainer.getComponentCount() > limitMsgCount){
                        if(mainContainer.getComponentCount() == (limitMsgCount + 1)) {
                            setUpExpandButton();
                            expandAllFrame.setVisible(true);
                        }
                        expandAllFrame.incMessageCount();
                    }
                    break;
                }
                case UPWARDS:{
                    if(mainContainer.getComponentCount() > (limitMsgCount + 1) && !expanded){
                        messagePanel.setVisible(false);
                    }
                    if(mainContainer.getComponentCount() > (limitMsgCount + 1)){
                        if(mainContainer.getComponentCount() == (limitMsgCount + 2)) {
                            setUpExpandButton();
                            expandAllFrame.setVisible(true);
                        }
                        expandAllFrame.incMessageCount();
                    }
                    break;
                }
            }
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
            Component panel = ((CloseMessagePanelEvent) event).getComponent();
            this.remove(panel);
            switch (flowDirections){
                case DOWNWARDS:{
                    if(mainContainer.getComponentCount() == 0){
                        this.setVisible(false);
                    }else if(mainContainer.getComponentCount() == limitMsgCount){
                        mainContainer.getComponent((limitMsgCount - 1)).setVisible(true);
                        expandAllFrame.decMessageCount();
                        expandAllFrame.setVisible(false);
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
                    }else if(mainContainer.getComponentCount() > (limitMsgCount + 1)) {
                        mainContainer.getComponent(mainContainer.getComponentCount() - limitMsgCount).setVisible(true);
                        expandAllFrame.decMessageCount();
                    }
                    break;
                }
            }
            this.pack();
        });
        EventRouter.INSTANCE.registerHandler(RepaintEvent.RepaintMessagePanel.class, event -> {
            IncMessageFrame.this.revalidate();
            IncMessageFrame.this.repaint();
        });
    }

    private void setUpExpandButton(){
        if(!inMoveMode) {
            switch (flowDirections) {
                case DOWNWARDS: {
                    if (mainContainer.getComponentCount() > limitMsgCount) {
                        Component[] components = mainContainer.getComponents();
                        int height = 0;
                        for (int i = 0; i < limitMsgCount; i++) {
                            height += components[i].getPreferredSize().height;
                        }
                        expandAllFrame.setMinimumSize(new Dimension(15, height));
                        expandAllFrame.setLocation(new Point(this.getLocation().x - expandAllFrame.getPreferredSize().width,
                                this.getLocation().y));
                        expandAllFrame.pack();
                    }
                    break;
                }
                case UPWARDS: {
                    if (mainContainer.getComponentCount() > (limitMsgCount + 1)) {
                        Component[] components = mainContainer.getComponents();
                        int height = 0;
                        for (int i = components.length - 1; i > components.length - (limitMsgCount + 1); i--) {
                            height += components[i].getPreferredSize().height;
                        }
                        Point location = mainContainer.getComponent(components.length - limitMsgCount).getLocationOnScreen();
                        expandAllFrame.setMinimumSize(new Dimension(15, height));
                        expandAllFrame.setLocation(new Point(this.getLocation().x - expandAllFrame.getPreferredSize().width,
                                location.y));
                        expandAllFrame.pack();
                    }
                    break;
                }
            }
        }else {
            expandAllFrame.changeArrowDirection();
        }
    }

    @Override
    protected JPanel panelWhenMove() {
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER,18f,"Notification panel"));

        JPanel outer = componentsFactory.getTransparentPanel(new BorderLayout());

        String arrowPath = (flowDirections.equals(FlowDirections.DOWNWARDS))? "app/downwards_arrow.png" : "app/upwards_arrow.png";
        JLabel arrow = componentsFactory.getIconLabel(arrowPath);
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
                    arrow.setIcon(componentsFactory.getImage("app/upwards_arrow.png"));
                    expandAllFrame.changeArrowDirection();
                    break;
                }
                case "Downwards":{
                    pikerDirection = FlowDirections.DOWNWARDS;
                    arrow.setIcon(componentsFactory.getImage("app/downwards_arrow.png"));
                    expandAllFrame.changeArrowDirection();
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
        growPanel.add(componentsFactory.getTextLabel("Limit messages:"),titleColumn);
        JPanel limitPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel limitCount = componentsFactory.getTextLabel(String.valueOf(limitMsgCount));
        JPanel limitCountPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        limitCountPanel.add(limitCount);
        limitCountPanel.setPreferredSize(new Dimension(12,30));

        limitSlider = componentsFactory.getSlider(3, 20, limitMsgCount);
        limitSlider.addChangeListener(e -> {
            limitCount.setText(String.valueOf(limitSlider.getValue()));
            repaint();
        });
        limitSlider.setPreferredSize(new Dimension(100,30));
        limitPanel.add(limitCountPanel);
        limitPanel.add(limitSlider);
        growPanel.add(limitPanel,fieldColumn);
        titleColumn.gridy = 2;
        fieldColumn.gridy = 2;
        titleColumn.insets = new Insets(-18,0,0,6);
        fieldColumn.insets = new Insets(-18,0,0,6);
        growPanel.add(componentsFactory.getTextLabel("Expanded messages:"),titleColumn);

        JPanel expandPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel expandCount = componentsFactory.getTextLabel(String.valueOf(expandedMsgCount));
        JPanel expandCountPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        expandCountPanel.add(expandCount);
        expandCountPanel.setPreferredSize(new Dimension(12,30));

        expandSlider = componentsFactory.getSlider(0, 20, expandedMsgCount);
        expandSlider.addChangeListener(e -> {
            expandCount.setText(String.valueOf(expandSlider.getValue()));
            repaint();
        });
        expandSlider.setPreferredSize(new Dimension(100,30));
        expandPanel.add(expandCountPanel);
        expandPanel.add(expandSlider);

        growPanel.add(expandPanel,fieldColumn);
        panel.add(labelPanel);
        panel.add(growPanel);
        outer.add(arrow,BorderLayout.LINE_START);
        outer.add(panel,BorderLayout.CENTER);
        outer.setPreferredSize(new Dimension(200, 110));
        return outer;
    }

    @Override
    protected void onLock() {
        if(!this.flowDirections.equals(pikerDirection)){
            configManager.setFlowDirection(pikerDirection.toString());
            changeDirectionTo(pikerDirection);
            expandAllFrame.changeArrowDirection();
            locationWasChanged = true;
        }
        if(limitMsgCount != limitSlider.getValue()) {
            limitMsgCount = limitSlider.getValue();
            configManager.setLimitMsgCount(limitMsgCount);
        }
        if(expandedMsgCount != expandSlider.getValue()) {
            expandedMsgCount = expandSlider.getValue();
            configManager.setLimitMsgCount(expandedMsgCount);
        }
        changeLocation();
        super.onLock();
        if(expandAllFrame.isVisible()) {
            setUpExpandButton();
        }
    }

    @Override
    protected void onUnlock() {
        super.onUnlock();
        if(expandAllFrame.isVisible()) {
            setUpExpandButton();
        }
    }

    private void changeDirectionTo(FlowDirections direction){
        hideComponent();
        switch (direction) {
            case DOWNWARDS:{
                mainContainer.remove(buffer);
                Component[] components = mainContainer.getComponents();
                for (Component component : components) {
                    if(tradeMode.equals(TradeMode.DEFAULT)) {
                        ((MessagePanel) component).setStyle(MessagePanelStyle.DOWNWARDS_SMALL);
                    }else {
                        ((MessagePanel) component).setStyle(MessagePanelStyle.SP_MODE);
                    }
                    mainContainer.remove(component);
                    mainContainer.add(component, 0);
                }
                break;
            }
            case UPWARDS: {
                mainContainer.add(buffer,0);
                Component[] components = mainContainer.getComponents();
                for (int i = 1; i < components.length; i++) {
                    if (tradeMode.equals(TradeMode.DEFAULT)) {
                        ((MessagePanel) components[i]).setStyle(MessagePanelStyle.UPWARDS_SMALL);
                    } else {
                        ((MessagePanel) components[i]).setStyle(MessagePanelStyle.SP_MODE);
                    }
                    mainContainer.remove(components[i]);
                    mainContainer.add(components[i], 1);
                }
                break;
            }
        }
        showComponent();
        this.flowDirections = direction;
    }
    private void changeLocation(){
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
        showComponent();
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
    }

    @Override
    public void onCollapseMessage() {
        this.pack();
        if(expandAllFrame.isVisible()) {
            setUpExpandButton();
        }
    }

    private class ExpandAllFrame extends OverlaidFrame {
        private int messageCount = 0;
        private JLabel msgCountLabel;
        private JButton expandButton;
        private  JPanel labelPanel;

        ExpandAllFrame() {
            super("MT-ExpandAll");
        }
        @Override
        protected void initialize() {
            this.setBackground(AppThemeColor.MSG_HEADER);
            this.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(1,1,1,0,AppThemeColor.TRANSPARENT),
                            BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.BORDER)));

            labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
            labelPanel.setBackground(AppThemeColor.MSG_HEADER);
            labelPanel.setBorder(BorderFactory.createEmptyBorder(0,-5,0,-5));
            msgCountLabel = componentsFactory.getTextLabel("+" + String.valueOf(messageCount));
            String iconPath = (flowDirections.equals(FlowDirections.DOWNWARDS))? "app/collapse-all.png" : "app/expand-all.png";
            expandButton = componentsFactory.getIconButton(iconPath,28,AppThemeColor.MSG_HEADER,"");
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
                        expandButton.setIcon(componentsFactory.getIcon(iconPath,28));
                        IncMessageFrame.this.pack();
                    }
                }
            });
            expandButton.setAlignmentY(SwingConstants.CENTER);
            labelPanel.add(msgCountLabel);
            if(flowDirections.equals(FlowDirections.DOWNWARDS)){
                this.add(expandButton,BorderLayout.CENTER);
                this.add(labelPanel,BorderLayout.PAGE_END);
            }else {
                this.add(labelPanel, BorderLayout.PAGE_START);
                this.add(expandButton, BorderLayout.CENTER);
            }
            this.pack();
        }

        void incMessageCount(){
            messageCount++;
            if(!expanded) {
                msgCountLabel.setText("+" + String.valueOf(messageCount));
                this.repaint();
            }

        }
        void decMessageCount(){
            messageCount--;
            if(!expanded) {
                msgCountLabel.setText("+" + String.valueOf(messageCount));
                this.repaint();
            }
        }
        void changeArrowDirection(){
            expandAllFrame.setLocation(new Point(IncMessageFrame.this.getLocation().x - expandAllFrame.getPreferredSize().width, IncMessageFrame.this.getLocation().y));
            expandAllFrame.setMinimumSize(new Dimension(15, IncMessageFrame.this.getPreferredSize().height));
            String iconPath = (pikerDirection.equals(FlowDirections.DOWNWARDS))? "app/collapse-all.png" : "app/expand-all.png";
            expandButton.setIcon(componentsFactory.getIcon(iconPath,28));
            this.remove(labelPanel);
            if(pikerDirection.equals(FlowDirections.DOWNWARDS)){
                this.add(labelPanel,BorderLayout.PAGE_END);
            }else {
                this.add(labelPanel, BorderLayout.PAGE_START);
            }
            this.pack();
        }
        @Override
        public void initHandlers() {

        }

        @Override
        protected LayoutManager getFrameLayout() {
            return new BorderLayout();
        }
    }
}
