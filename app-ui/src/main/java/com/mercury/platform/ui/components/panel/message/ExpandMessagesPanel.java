package com.mercury.platform.ui.components.panel.message;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.shared.entity.message.FlowDirections;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExpandMessagesPanel extends JPanel{
    private JLabel messageCountLabel;
    private int limit;
    private int messageCount;
    private JPanel rootPanel;
    private JPanel filler;
    private ComponentsFactory componentsFactory;
    private boolean expanded;

    public ExpandMessagesPanel(@NonNull ComponentsFactory componentsFactory, int limit, int current) {
        super(new BorderLayout());
        this.limit = limit;
        this.componentsFactory = componentsFactory;
        this.messageCount = current;
        this.setBackground(AppThemeColor.TRANSPARENT);
        this.initRootPanel();
        this.filler = new JPanel(new BorderLayout());
        this.filler.setBackground(AppThemeColor.TRANSPARENT);
        JPanel fillerContent = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        fillerContent.setBackground(AppThemeColor.TRANSPARENT);
        fillerContent.setPreferredSize(new Dimension((int)(23 * componentsFactory.getScale()),(int)(22 * componentsFactory.getScale())));
        fillerContent.setBorder(BorderFactory.createEmptyBorder(-4,0,0,0));
        this.filler.add(fillerContent,BorderLayout.PAGE_START);
        if(current > limit){
            this.add(this.rootPanel, BorderLayout.CENTER);
        }else {
            this.add(this.filler,BorderLayout.CENTER);
        }
    }

    private void initRootPanel(){
        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1,1,1,0, AppThemeColor.TRANSPARENT),
                BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.BORDER)));
        this.rootPanel.setBackground(AppThemeColor.MSG_HEADER);

        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.setBackground(AppThemeColor.MSG_HEADER);
        labelPanel.setPreferredSize(new Dimension((int)(10 * componentsFactory.getScale()),(int)(22 * componentsFactory.getScale())));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(-4,0,0,0));

        messageCountLabel = componentsFactory.getTextLabel("+" + String.valueOf(messageCount));
        FlowDirections flowDirections = FlowDirections.valueOf(ConfigManager.INSTANCE.getFlowDirection());
        String iconPath = (flowDirections.equals(FlowDirections.DOWNWARDS))? "app/collapse-all.png" : "app/expand-all.png";
        JButton expandButton = componentsFactory.getIconButton(iconPath,22,AppThemeColor.MSG_HEADER,"");
        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    String iconPath;
                    if(!expanded){
                        if(flowDirections.equals(FlowDirections.DOWNWARDS)){
                            iconPath = "app/expand-all.png";
                        }else {
                            iconPath = "app/collapse-all.png";
                        }
                        messageCountLabel.setText("");
                        expanded = true;
                        MercuryStoreUI.INSTANCE.expandMessageSubject.onNext(true);
                    }else {
                        if(flowDirections.equals(FlowDirections.DOWNWARDS)){
                            iconPath = "app/collapse-all.png";
                        }else {
                            iconPath = "app/expand-all.png";
                        }
                        messageCountLabel.setText("+"+String.valueOf(messageCount - limit));
                        expanded = false;
                        MercuryStoreUI.INSTANCE.collapseMessageSubject.onNext(true);
                    }
                    expandButton.setIcon(componentsFactory.getIcon(iconPath,22));
                }
            }
        });
        expandButton.setAlignmentY(SwingConstants.CENTER);
        labelPanel.add(messageCountLabel);
        if(flowDirections.equals(FlowDirections.DOWNWARDS)){
            this.rootPanel.add(expandButton,BorderLayout.CENTER);
            this.rootPanel.add(labelPanel,BorderLayout.PAGE_END);
        }else {
            this.rootPanel.add(labelPanel, BorderLayout.PAGE_START);
            this.rootPanel.add(expandButton, BorderLayout.CENTER);
        }
    }

    public void increment() {
        this.messageCount++;
        if(!expanded) {
            this.messageCountLabel.setText("+" + String.valueOf(messageCount - limit));
        }
        if(this.messageCount == this.limit + 1){
            this.removeAll();
            this.add(this.rootPanel,BorderLayout.CENTER);
        }
    }
    public void decrement() {
        this.messageCount--;
        if(!expanded) {
            this.messageCountLabel.setText("+" + String.valueOf(messageCount - limit));
        }
        if(this.messageCount == this.limit){
            this.removeAll();
            this.add(this.filler,BorderLayout.CENTER);
        }
    }
}
