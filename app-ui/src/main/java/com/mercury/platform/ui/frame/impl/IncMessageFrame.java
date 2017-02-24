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
public class IncMessageFrame extends MovableComponentFrame{
    private final Logger logger = LogManager.getLogger(IncMessageFrame.class.getSimpleName());
    private TradeMode tradeMode;
    private FlowDirections flowDirections;
    private FlowDirections pikerDirection;
    private boolean wasVisible;
    private boolean expanded = false;
    private JPanel buffer;

    private ExpandAllFrame expandAllFrame;

    private boolean dnd = false;
    public IncMessageFrame(){
        super("MT-IncMessagesFrame");
        processSEResize = false;
        tradeMode = TradeMode.valueOf(configManager.getTradeMode());
        flowDirections = FlowDirections.valueOf(configManager.getFlowDirection());
        pikerDirection = FlowDirections.valueOf(configManager.getFlowDirection());
        expandAllFrame = new ExpandAllFrame();
        buffer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buffer.setMinimumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        buffer.setBorder(BorderFactory.createLineBorder(AppThemeColor.TEXT_IMPORTANT));
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setBackground(AppThemeColor.FRAME_ALPHA);
        this.getRootPane().setBorder(null);
        if(flowDirections.equals(FlowDirections.UPWARDS)){
            changeDirectionTo(FlowDirections.UPWARDS);
            changeLocation();
        }
        expandAllFrame.init();
    }

    private void convertFrameTo(TradeMode mode){
        switch (mode){
            case DEFAULT:{
                if(tradeMode == TradeMode.SUPER){
                    int deltaY = 0;
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        int oldH = messagePanel.getPreferredSize().height;
                        if(flowDirections.equals(FlowDirections.DOWNWARDS)) {
                            ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.DOWNWARDS_SMALL);
                        }else {
                            ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.UPWARDS_SMALL);
                            int newH = messagePanel.getPreferredSize().height;
                            deltaY += oldH-newH;
                        }
                    }
                    if(deltaY > 0){
                        this.setLocation(this.getLocation().x, this.getLocation().y + deltaY);
                    }
                }
                break;
            }
            case SUPER:{
                if(tradeMode == TradeMode.DEFAULT){
                    int deltaY = 0;
                    Component[] components = mainContainer.getComponents();
                    for (Component messagePanel : components) {
                        int oldH = messagePanel.getPreferredSize().height;
                        ((MessagePanel) messagePanel).setStyle(MessagePanelStyle.SP_MODE);
                        if(flowDirections.equals(FlowDirections.UPWARDS)) {
                            int newH = messagePanel.getPreferredSize().height;
                            deltaY += newH-oldH;
                        }
                    }
                    if(deltaY > 0){
                        this.setLocation(this.getLocation().x, this.getLocation().y - deltaY);
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
                if(mainContainer.getComponentCount() > 2 && !expanded){
                    messagePanel.setVisible(false);
                }else {
                    this.setLocation(new Point(this.getLocation().x, this.getLocation().y - messagePanel.getPreferredSize().height));
                }
                mainContainer.add(messagePanel, 0);
            }else {
                if(mainContainer.getComponentCount() > 2 && !expanded){
                    messagePanel.setVisible(false);
                }
                mainContainer.add(messagePanel);
            }
            this.pack();
            if(mainContainer.getComponentCount() > 3){
                expandAllFrame.incMessageCount();
                if(mainContainer.getComponentCount() == 4) {
                    expandAllFrame.setLocation(new Point(this.getLocation().x - expandAllFrame.getPreferredSize().width, this.getLocation().y));
                    expandAllFrame.setMinimumSize(new Dimension(15, this.getPreferredSize().height));
                    expandAllFrame.pack();
                    expandAllFrame.setVisible(true);
                }
            }
        });
        EventRouter.INSTANCE.registerHandler(CloseMessagePanelEvent.class, event -> {
            Component panel = ((CloseMessagePanelEvent) event).getComponent();
            if(mainContainer.getComponentCount() == 1){
                this.setVisible(false);
            }else {
                if(flowDirections.equals(FlowDirections.UPWARDS)){
                    this.removeUpwardsSpace(panel.getHeight());
                }
            }
            if(mainContainer.getComponentCount() < 5) {
                expandAllFrame.setVisible(false);
                Arrays.stream(mainContainer.getComponents()).forEach(component ->{
                    if(!component.isVisible()){
                        if(flowDirections.equals(FlowDirections.UPWARDS)){
                            this.setLocation(new Point(this.getLocation().x, this.getLocation().y - component.getPreferredSize().height));
                        }
                        component.setVisible(true);
                    }
                });
            }else {
                if(flowDirections.equals(FlowDirections.DOWNWARDS)){
                    mainContainer.getComponent(3).setVisible(true);
                }else {
                    Component component = mainContainer.getComponent(mainContainer.getComponentCount() - 1 - 3);
                    this.setLocation(new Point(this.getLocation().x, this.getLocation().y - component.getPreferredSize().height));
                    component.setVisible(true);
                }
            }
            if(mainContainer.getComponentCount() > 3){
                expandAllFrame.decMessageCount();
            }
            this.remove(panel);
            this.pack();
        });
        EventRouter.INSTANCE.registerHandler(RepaintEvent.RepaintMessagePanel.class, event -> {
            IncMessageFrame.this.revalidate();
            IncMessageFrame.this.repaint();
        });
    }

    @Override
    protected JPanel panelWhenMove() {
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MESSAGE, TextAlignment.CENTER,20f,"Notification panel"));

        JPanel outer = componentsFactory.getTransparentPanel(new BorderLayout());

        String arrowPath = (flowDirections.equals(FlowDirections.DOWNWARDS))? "app/downwards_arrow.png" : "app/upwards_arrow.png";
        JLabel arrow = componentsFactory.getIconLabel(arrowPath);
        JPanel growPanel = componentsFactory.getTransparentPanel(new GridBagLayout());
        GridBagConstraints constraint = new GridBagConstraints();
        constraint.gridy = 0;
        constraint.gridx = 0;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.weightx = 0.5f;
        constraint.insets = new Insets(2,0,0,2);
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

        growPanel.add(componentsFactory.getTextLabel("Flow direction:"),constraint);
        constraint.gridx = 1;
        growPanel.add(flowDirectionPicker,constraint);
        constraint.gridx = 0;
        constraint.gridy = 1;
        growPanel.add(componentsFactory.getTextLabel("Expand first:"),constraint);
        constraint.gridx = 1;
        JCheckBox expandFirst = new JCheckBox();
        expandFirst.setBackground(AppThemeColor.TRANSPARENT);
        growPanel.add(expandFirst,constraint);

        panel.add(labelPanel);
        panel.add(growPanel);
        outer.add(arrow,BorderLayout.LINE_START);
        outer.add(panel,BorderLayout.CENTER);
        outer.setPreferredSize(new Dimension(200, 119));
        return outer;
    }

    @Override
    protected void onLock() {
        if(!this.flowDirections.equals(pikerDirection)){
            configManager.setFlowDirection(pikerDirection.toString());
            changeDirectionTo(pikerDirection);
            changeLocation();
            expandAllFrame.changeArrowDirection();
        }else if(locationWasChanged){
            locationWasChanged = false;
            changeLocation();
            expandAllFrame.changeArrowDirection();
        }
        super.onLock();
    }

    @Override
    protected void onUnlock() {
        super.onUnlock();
        expandAllFrame.changeArrowDirection();
    }

    private void changeDirectionTo(FlowDirections direction){
        wasVisible = this.isVisible();
        hideComponent();
        switch (direction) {
            case DOWNWARDS:{
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
                Component[] components = mainContainer.getComponents();
                for (Component component : components) {
                    if (tradeMode.equals(TradeMode.DEFAULT)) {
                        ((MessagePanel) component).setStyle(MessagePanelStyle.UPWARDS_SMALL);
                    } else {
                        ((MessagePanel) component).setStyle(MessagePanelStyle.SP_MODE);
                    }
                    mainContainer.remove(component);
                    mainContainer.add(component, 0);
                }
                break;
            }
        }
        this.flowDirections = direction;
    }
    private void changeLocation(){
        switch (flowDirections){
            case DOWNWARDS:{
                this.setLocation(ConfigManager.INSTANCE.getFrameSettings(this.getClass().getSimpleName()).getFrameLocation());
                break;
            }
            case UPWARDS:{
                int deltaY = 0;
                Component[] components = mainContainer.getComponents();
                for (Component component : components) {
                    if(component.isVisible()) {
                        deltaY += component.getPreferredSize().height;
                    }
                }
                if(deltaY == 0){
                    this.setLocation(this.getLocation().x, this.getLocation().y + 119);
                }else {
                    this.setLocation(this.getLocation().x, this.getLocation().y - deltaY + 119);
                }
            }
        }
        if(wasVisible){
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
    public void pack() {
        super.pack();
        expandAllFrame.setLocation(new Point(IncMessageFrame.this.getLocation().x - expandAllFrame.getPreferredSize().width, IncMessageFrame.this.getLocation().y));
        expandAllFrame.setMinimumSize(new Dimension(15, IncMessageFrame.this.getPreferredSize().height));
        expandAllFrame.pack();
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
                public void mouseClicked(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)){
                        String iconPath;
                        if(!expanded){
                            Arrays.stream(mainContainer.getComponents()).forEach(panel ->{
                                if(!panel.isVisible()){
                                    if(flowDirections.equals(FlowDirections.UPWARDS)){
                                        IncMessageFrame.this.setLocation(new Point(IncMessageFrame.this.getLocation().x, IncMessageFrame.this.getLocation().y - panel.getPreferredSize().height));
                                    }
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
                                for (int i = 0; i < mainContainer.getComponentCount() - 1 - 2; i++) {
                                    components[i].setVisible(false);
                                    removeUpwardsSpace(components[i].getPreferredSize().height);
                                }
                            }else {
                                for (int i = 0; i < components.length; i++) {
                                    if(i > 2){
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
            msgCountLabel.setText("+"+String.valueOf(messageCount));
            this.repaint();
        }
        void decMessageCount(){
            messageCount--;
            msgCountLabel.setText("+"+String.valueOf(messageCount));
            this.repaint();
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
