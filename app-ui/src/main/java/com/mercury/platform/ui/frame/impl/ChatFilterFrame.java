package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AddInterceptorEvent;
import com.mercury.platform.shared.events.custom.ChatFilterMessageEvent;
import com.mercury.platform.shared.events.custom.RemoveInterceptorEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.components.panel.chat.ChatFilterPanel;
import com.mercury.platform.ui.frame.MovableComponentFrame;
import com.mercury.platform.ui.frame.TitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 05.01.2017.
 */
public class ChatFilterFrame extends MovableComponentFrame {
    private ChunkMessagesPicker msgPicker;
    private ChatFilterPanel msgContainer;
    private boolean soundEnable = false;

    private JTextField textField;
    public ChatFilterFrame() {
        super("MT-ChatFilter");
        this.setVisible(false);
        prevState = FrameStates.HIDE;

        FrameSettings frameSettings = configManager.getFrameSettings(this.getClass().getSimpleName());
        this.setPreferredSize(frameSettings.getFrameSize());
    }

    @Override
    protected void initialize() {
        super.initialize();
        msgPicker = new ChunkMessagesPicker();
        msgPicker.init();

        msgContainer = new ChatFilterPanel(this);

        this.add(getTopPanel(),BorderLayout.PAGE_START);
        this.add(msgContainer,BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void initHandlers() {

    }

    private JPanel getTopPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(4,4,0,4));

        textField = componentsFactory.getTextField("");
        textField.setPreferredSize(new Dimension(130,18));
        textField.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        textField.setBackground(AppThemeColor.SLIDE_BG);

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton edit = componentsFactory.getIconButton("app/edit.png", 18, AppThemeColor.TRANSPARENT, "Edit");
        edit.setBorder(null);
        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                msgPicker.showComponent();
            }
        });
        JButton clear = componentsFactory.getIconButton("app/clear-icon.png", 18, AppThemeColor.TRANSPARENT, "Clear window.");
        clear.setBorder(null);
        clear.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                msgContainer.clear();
                pack();
                repaint();
            }
        });
        JButton sound = componentsFactory.getIconButton("app/sound-disable.png", 18, AppThemeColor.TRANSPARENT, "Enable sound notification.");
        sound.setBorder(null);
        sound.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(soundEnable){
                    sound.setIcon(componentsFactory.getIcon("app/sound-disable.png",18));
                    soundEnable = false;
                    ChatFilterFrame.this.repaint();
                }else {
                    sound.setIcon(componentsFactory.getIcon("app/sound-enable.png",18));
                    soundEnable = true;
                    ChatFilterFrame.this.repaint();
                }
            }
        });
        miscPanel.add(edit);
        miscPanel.add(clear);
        miscPanel.add(sound);

        root.add(miscPanel,BorderLayout.LINE_END);
        root.add(textField,BorderLayout.CENTER);
        return root;
    }

    @Override
    protected JPanel panelWhenMove() {
        JPanel panel = componentsFactory.getTransparentPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        JPanel labelPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(componentsFactory.getTextLabel(FontStyle.BOLD, AppThemeColor.TEXT_MESSAGE, TextAlignment.LEFTOP,20f,"Chat filter"));
        panel.add(labelPanel);
        return panel;
    }

    @Override
    protected int getMinComponentCount() {
        return 2;
    }

    @Override
    protected LayoutManager getFrameLayout() {
        return new BorderLayout();
    }

    private class ChunkMessagesPicker extends TitledComponentFrame{
        private MessageInterceptor interceptor;

        protected ChunkMessagesPicker() {
            super("MT-ChatFilter-picker");
            this.setFocusableWindowState(true);
            this.setFocusable(true);
            this.setAlwaysOnTop(false);
            this.processingHideEvent = false;

            FrameSettings settings = ConfigManager.INSTANCE.getDefaultFramesSettings().get(ChatFilterFrame.class.getSimpleName());
            this.setMinimumSize(settings.getFrameSize());
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        }

        @Override
        protected void initialize() {
            super.initialize();

            JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
            root.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

            JTextField chunks = componentsFactory.getTextField("wtb, wts, exalt");
            chunks.setPreferredSize(new Dimension(130,18));
            chunks.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
            chunks.setBackground(AppThemeColor.SLIDE_BG);

            JButton save = componentsFactory.getBorderedButton("Save");
            save.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    textField.setFont(componentsFactory.getFontByLang(chunks.getText(), FontStyle.REGULAR).deriveFont(16f));
                    textField.setText(chunks.getText());

                    if (interceptor != null) {
                        EventRouter.INSTANCE.fireEvent(new RemoveInterceptorEvent(interceptor));
                    }
                    interceptor = new MessageInterceptor() {
                        @Override
                        protected void process(String message) {
                            msgContainer.addMessage(message);
                            if(soundEnable){
                                EventRouter.INSTANCE.fireEvent(new ChatFilterMessageEvent());
                            }
                            ChatFilterFrame.this.pack();
                        }

                        @Override
                        protected MessageFilter getFilter() {
                            return message -> {
                                message = message.toLowerCase();
                                String stubChunks = chunks.getText();
                                String chunks = StringUtils.deleteWhitespace(stubChunks);
                                String[] chunksArray = StringUtils.split(chunks, ",");

                                for (String chunk : chunksArray) {
                                    chunk = chunk.toLowerCase();
                                    if(message.contains(chunk)){
                                        return true;
                                    }
                                }
                                return false;
                            };
                        }
                    };
                    EventRouter.INSTANCE.fireEvent(new AddInterceptorEvent(interceptor));
                    hideComponent();
                }
            });
            root.add(chunks,BorderLayout.CENTER);
            root.add(save,BorderLayout.LINE_END);
            this.add(root,BorderLayout.CENTER);
            this.pack();
        }

        @Override
        public void initHandlers() {

        }

        @Override
        protected String getFrameTitle() {
            return "Message chunks";
        }
    }
}
