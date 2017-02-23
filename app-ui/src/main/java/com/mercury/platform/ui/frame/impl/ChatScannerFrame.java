package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.core.utils.interceptor.MessageInterceptor;
import com.mercury.platform.core.utils.interceptor.filter.MessageFilter;
import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.AddInterceptorEvent;
import com.mercury.platform.shared.events.custom.AddShowDelayEvent;
import com.mercury.platform.shared.events.custom.ChatFilterMessageEvent;
import com.mercury.platform.shared.events.custom.RemoveInterceptorEvent;
import com.mercury.platform.shared.pojo.FrameSettings;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.chat.ChatFilterPanel;
import com.mercury.platform.ui.frame.TitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/**
 * Created by Константин on 05.01.2017.
 */
public class ChatScannerFrame extends TitledComponentFrame {
    private ChunkMessagesPicker msgPicker;
    private ChatFilterPanel msgContainer;
    private boolean soundEnable = false;

    private JList<String> list;
    public ChatScannerFrame() {
        super("MT-ChatFilter");
        FrameSettings frameSettings = configManager.getFrameSettings(this.getClass().getSimpleName());
        this.setPreferredSize(frameSettings.getFrameSize());
    }

    @Override
    protected void initialize() {
        super.initialize();
        msgPicker = new ChunkMessagesPicker();
        msgPicker.init();

        msgContainer = new ChatFilterPanel(this);
        msgContainer.setPreferredSize(new Dimension(400,100));

        JSplitPane root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        BasicSplitPaneUI ui = (BasicSplitPaneUI)root.getUI();
        ui.getDivider().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                repaint();
            }

        });
        ui.getDivider().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                repaint();
            }
        });
        ui.getDivider().setBorder(BorderFactory.createMatteBorder(0,2,0,2,AppThemeColor.BORDER));
        root.setDividerLocation(0.5);
        root.setDividerSize(4);
        root.setBorder(null);
        root.setBackground(AppThemeColor.FRAME);
        root.setLeftComponent(msgContainer);
        root.setRightComponent(getTopPanel());
        this.add(root,BorderLayout.CENTER);
        this.pack();
    }

    @Override
    protected String getFrameTitle() {
        return "Chat scanner";
    }

    @Override
    public void initHandlers() {

    }

    private JPanel getTopPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));

        list = new JList<>(new String[]{"wtb","wts","exalt"});
        list.setLayoutOrientation(JList.VERTICAL);
        list.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
        list.setBackground(AppThemeColor.SLIDE_BG);
        list.setForeground(AppThemeColor.TEXT_DEFAULT);
        list.setFont(componentsFactory.getFontByLang("template",FontStyle.REGULAR).deriveFont(16f));

        JPanel miscPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
        JButton edit = componentsFactory.getIconButton("app/edit.png", 18, AppThemeColor.TRANSPARENT, "");
        edit.setBorder(null);
        edit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                msgPicker.setLocation(e.getLocationOnScreen());
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
                    ChatScannerFrame.this.repaint();
                }else {
                    sound.setIcon(componentsFactory.getIcon("app/sound-enable.png",18));
                    soundEnable = true;
                    ChatScannerFrame.this.repaint();
                }
            }
        });
        miscPanel.add(edit);
        miscPanel.add(clear);
        miscPanel.add(sound);
        miscPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                repaint();
            }
        });

        root.add(miscPanel,BorderLayout.PAGE_START);
        root.add(list,BorderLayout.CENTER);
        return root;
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

            FrameSettings settings = ConfigManager.INSTANCE.getDefaultFramesSettings().get(ChatScannerFrame.class.getSimpleName());
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
                    list.setFont(componentsFactory.getFontByLang(chunks.getText(), FontStyle.REGULAR).deriveFont(16f));
                    String chunkStr = StringUtils.deleteWhitespace(chunks.getText());;
                    String[] split = chunkStr.split(",");
                    DefaultListModel<String> model = new DefaultListModel<>();
                    Arrays.stream(split).forEach(model::addElement);
                    list.setModel(model);

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
                            ChatScannerFrame.this.pack();
                        }

                        @Override
                        protected MessageFilter getFilter() {
                            return message -> {
                                message = message.toLowerCase();
                                String stubChunks = chunks.getText();
                                String chunks = StringUtils.deleteWhitespace(stubChunks);
                                String[] chunksArray = StringUtils.split(chunks, ",");
                                if(!message.contains("] $") && !message.contains("] #")){
                                    System.out.println(message);
                                    return false;
                                }
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
                    EventRouter.INSTANCE.fireEvent(new AddShowDelayEvent());
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
            return "Search for:";
        }
    }
}
