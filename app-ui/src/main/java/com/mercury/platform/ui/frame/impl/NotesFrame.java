package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.TitledComponentFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.note.Note;
import com.mercury.platform.ui.misc.note.NotesLoader;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by Константин on 20.01.2017.
 */
public class NotesFrame extends TitledComponentFrame {
    private List<Note> currentNotes;
    private ContentPanel contentPanel;

    private JCheckBox showOnStartUp;
    public NotesFrame() {
        super("MT-NotesFrame");
        this.setVisible(false); //todo from config manager
    }

    @Override
    protected void initialize() {
        super.initialize();
        processEResize = false;
        processSEResize = false;

        currentNotes = new NotesLoader().getNotes("test");

        JPanel rootPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        contentPanel = new ContentPanel();
        rootPanel.add(contentPanel,BorderLayout.CENTER);
        JPanel miscPanel = componentsFactory.getTransparentPanel(new BorderLayout());

        showOnStartUp = new JCheckBox();
        showOnStartUp.setBackground(AppThemeColor.TRANSPARENT);
        showOnStartUp.setSelected(ConfigManager.INSTANCE.isShowOnStartUp());

        JPanel showOnStartPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        showOnStartPanel.add(showOnStartUp);
        showOnStartPanel.add(componentsFactory.getTextLabel(FontStyle.REGULAR,AppThemeColor.TEXT_DEFAULT,TextAlignment.LEFTOP,15f,"Show on StartUp"));
        miscPanel.add(showOnStartPanel,BorderLayout.CENTER);
        miscPanel.add(getNavBar(),BorderLayout.PAGE_END);
        rootPanel.add(miscPanel,BorderLayout.PAGE_END);
        this.add(rootPanel,BorderLayout.CENTER);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(ConfigManager.INSTANCE.isShowOnStartUp());
    }
    private JPanel getNavBar(){
        JPanel navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.RIGHT));
        Dimension dimension = new Dimension(80, 26);
        JButton previous = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME,
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.BORDER),
                        BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 3)
                ),
                "Previous",
                14f);
        previous.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                contentPanel.prev();
                NotesFrame.this.repaint();
            }
        });
        JButton next = componentsFactory.getBorderedButton("Next");
        next.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                contentPanel.next();
                NotesFrame.this.repaint();
            }
        });
        JButton close = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME,
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                "Close",
                14f);
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                NotesFrame.this.setVisible(false);
                ConfigManager.INSTANCE.saveProperty("showOnStartUp", showOnStartUp.isSelected());
                FramesManager.INSTANCE.enableMovement();
            }
        });
        close.setBackground(AppThemeColor.FRAME);

        previous.setPreferredSize(dimension);
        next.setPreferredSize(dimension);
        close.setPreferredSize(dimension);

        navBar.add(previous);
        navBar.add(next);
        navBar.add(close);
        return navBar;
    }
    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Welcome to the MercuryTrade";
    }

    private class ContentPanel extends JPanel{
        private int noteIndex = 0;
        private JLabel titleLabel;
        private ContentPanel() {
            super(new BorderLayout());
            this.setBackground(AppThemeColor.TRANSPARENT);
            titleLabel = componentsFactory.getTextLabel(FontStyle.BOLD,AppThemeColor.TEXT_MISC, TextAlignment.LEFTOP,20f,"");
            titleLabel.setBorder(null);
            renderCurrentNote();
        }
        private void renderCurrentNote(){
            this.removeAll();
            Note note = currentNotes.get(noteIndex);
            titleLabel.setText(note.getTitle());
            JPanel innerContent = new JPanel(new BorderLayout());
            innerContent.setBackground(AppThemeColor.SLIDE_BG);
            innerContent.setBorder(BorderFactory.createLineBorder(AppThemeColor.HEADER));
            innerContent.setPreferredSize(new Dimension(530,260));

            JLabel image = componentsFactory.getIconLabel(note.getImagePath());
            JTextArea textArea = componentsFactory.getSimpleTextAre(note.getText());
            switch (note.getLayout()){
                case VERTICAL:{
                    innerContent.setLayout(new BoxLayout(innerContent,BoxLayout.Y_AXIS));
                    break;
                }
                case HORIZONTAL:{
                    innerContent.setLayout(new FlowLayout(FlowLayout.LEFT));
                    textArea.setPreferredSize(new Dimension(160,200));
                    break;
                }
            }
            textArea.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
            JPanel imgPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
            JPanel textPanel = componentsFactory.getTransparentPanel(new BorderLayout());
            textPanel.add(textArea,BorderLayout.CENTER);
            imgPanel.add(image);
            innerContent.add(imgPanel);
            innerContent.add(textPanel);

            this.add(titleLabel,BorderLayout.PAGE_START);
            this.add(innerContent,BorderLayout.CENTER);
            NotesFrame.this.pack();
            NotesFrame.this.repaint();
        }
        void next(){
            if(noteIndex < currentNotes.size()-1){
                noteIndex++;
                renderCurrentNote();
            }
        }
        void prev(){
            if(noteIndex > 0){
                noteIndex--;
                renderCurrentNote();
            }
        }

    }
}
