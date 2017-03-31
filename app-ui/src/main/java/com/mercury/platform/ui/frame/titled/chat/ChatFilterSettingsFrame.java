package com.mercury.platform.ui.frame.titled.chat;

import com.mercury.platform.ui.frame.titled.TitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class ChatFilterSettingsFrame extends TitledComponentFrame {
    private ChatSettingsCallback callback;

    public ChatFilterSettingsFrame(ChatSettingsCallback callback) {
        super("MercuryTrade");
        this.callback = callback;
        processingHideEvent = false;
        this.setFocusableWindowState(true);
        this.setFocusable(true);
        this.setAlwaysOnTop(false);
    }

    @Override
    protected void initialize() {
        super.initialize();
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

        JTextField chunks = componentsFactory.getTextField("wtb, wts, boss, rigwald,perandus,invasion");
        chunks.setPreferredSize(new Dimension(130,18));
        chunks.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        chunks.setBackground(AppThemeColor.SLIDE_BG);

        JButton save = componentsFactory.getBorderedButton("Save");
        save.addActionListener(action -> {
            String chunkStr = StringUtils.deleteWhitespace(chunks.getText());;
            String[] split = chunkStr.split(",");

            callback.onHide(split);
            hideComponent();
        });
        root.add(chunks,BorderLayout.CENTER);
        root.add(save,BorderLayout.LINE_END);
        this.add(root,BorderLayout.CENTER);
        this.pack();
    }

    public void showAuxiliaryFrame(Point location, int height) {
        this.setPreferredSize(new Dimension(this.getPreferredSize().width,height));
        this.setMinimumSize(new Dimension(this.getPreferredSize().width,height));
        this.setMinimumSize(new Dimension(this.getPreferredSize().width,height));
        this.setLocation(location);
        showComponent();
    }

    @Override
    protected String getFrameTitle() {
        return "Chat filter settings";
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
