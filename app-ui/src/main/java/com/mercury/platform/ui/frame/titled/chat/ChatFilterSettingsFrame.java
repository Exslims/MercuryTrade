package com.mercury.platform.ui.frame.titled.chat;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.titled.AbstractTitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

public class ChatFilterSettingsFrame extends AbstractTitledComponentFrame {
    private ChatSettingsCallback callback;
    private JTextField quickResponseField;

    public ChatFilterSettingsFrame(ChatSettingsCallback callback) {
        super();
        this.callback = callback;
        this.processingHideEvent = false;
        this.setFocusableWindowState(true);
        this.setFocusable(true);
        this.setAlwaysOnTop(false);
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setPreferredSize(new Dimension(350,300));
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel setupArea = componentsFactory.getTransparentPanel(new BorderLayout());
        setupArea.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

        JLabel title = componentsFactory.getTextLabel(
                FontStyle.REGULAR,
                AppThemeColor.TEXT_DEFAULT,
                TextAlignment.LEFTOP,
                15f,
                "Show messages containing the following words:");
        title.setBorder(BorderFactory.createEmptyBorder(2,0,6,0));
        JTextArea words = componentsFactory.getSimpleTextArea(ConfigManager.INSTANCE.getDefaultWords());
        words.setEditable(true);
        words.setCaretColor(AppThemeColor.TEXT_DEFAULT);
        words.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        words.setBackground(AppThemeColor.SLIDE_BG);

        JPanel navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        Dimension buttonSize = new Dimension(90, 24);
        JButton save = componentsFactory.getBorderedButton("Save");
        save.addActionListener(action -> {
            ConfigManager.INSTANCE.setDefaultWords(words.getText());
            ConfigManager.INSTANCE.setQuickResponse(quickResponseField.getText());

            String chunkStr = StringUtils.deleteWhitespace(words.getText());
            String[] split = chunkStr.split(",");

            this.callback.onHide(split);
            this.hideComponent();
        });
        JButton cancel = componentsFactory.getBorderedButton("Cancel");
        cancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 3)
        ));
        cancel.setBackground(AppThemeColor.FRAME);
        cancel.addActionListener(action -> {
            hideComponent();
        });
        save.setPreferredSize(buttonSize);
        cancel.setPreferredSize(buttonSize);
        navBar.add(cancel);
        navBar.add(save);

        setupArea.add(title,BorderLayout.PAGE_START);
        setupArea.add(words,BorderLayout.CENTER);

        root.add(setupArea,BorderLayout.CENTER);
        root.add(getMemo(),BorderLayout.LINE_END);

        JPanel padding = componentsFactory.getTransparentPanel(new BorderLayout());
        padding.setBorder(BorderFactory.createEmptyBorder(0,4,4,4));
        padding.add(getResponsePanel(),BorderLayout.CENTER);
        root.add(padding,BorderLayout.PAGE_END);
        this.add(root,BorderLayout.CENTER);
        this.add(navBar,BorderLayout.PAGE_END);
        this.pack();
    }
    private JPanel getResponsePanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JPanel setupArea = componentsFactory.getTransparentPanel(new BorderLayout());
        setupArea.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

        root.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        root.setBackground(AppThemeColor.SLIDE_BG);

        JButton quickResponse = componentsFactory.getIconButton("app/chat_scanner_response.png", 18f, AppThemeColor.SLIDE_BG, "Quick response");
        this.quickResponseField = componentsFactory.getTextField(ConfigManager.INSTANCE.getQuickResponse(),FontStyle.REGULAR,16f);
        this.quickResponseField.setBackground(AppThemeColor.SLIDE_BG);
        this.quickResponseField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppThemeColor.HEADER,1),
                BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT,3)
        ));
        setupArea.add(quickResponse,BorderLayout.LINE_START);
        setupArea.add(this.quickResponseField,BorderLayout.CENTER);
        root.add(setupArea,BorderLayout.CENTER);
        return root;
    }

    private JPanel getMemo(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        JLabel title = componentsFactory.getTextLabel(
                "Memo:",
                FontStyle.REGULAR);
        title.setBorder(BorderFactory.createEmptyBorder(6,0,2,0));


        JPanel itemsPanel = componentsFactory.getTransparentPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel,BoxLayout.Y_AXIS));

        itemsPanel.add(componentsFactory.getTextLabel("not case sensitive",FontStyle.REGULAR,17));
        itemsPanel.add(componentsFactory.getTextLabel("! - NOT (!wtb,!wts)",FontStyle.REGULAR,17));
        itemsPanel.add(componentsFactory.getTextLabel(", - separator",FontStyle.REGULAR,17));
        root.add(title,BorderLayout.PAGE_START);
        root.add(itemsPanel,BorderLayout.CENTER);
        return root;
    }

    public void showAuxiliaryFrame(Point location, int height) {
        this.setPreferredSize(new Dimension(this.getPreferredSize().width,height));
        this.setMinimumSize(new Dimension(this.getPreferredSize().width,height));
        this.setMinimumSize(new Dimension(this.getPreferredSize().width,height));
        this.setLocation(location);
        this.showComponent();
    }

    @Override
    protected String getFrameTitle() {
        return "Chat filter settings";
    }

    @Override
    public void subscribe() {

    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }
}
