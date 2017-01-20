package com.mercury.platform.ui.frame.impl;

import com.mercury.platform.shared.FrameStates;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.frame.TitledComponentFrame;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.note.Note;
import com.mercury.platform.ui.misc.note.NotesLoader;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.List;

/**
 * Created by Константин on 20.01.2017.
 */
public class NotesFrame extends TitledComponentFrame {
    private List<Note> currentNotes;
    private ContentPanel contentPanel;
    public NotesFrame() {
        super("MT-NotesFrame");
        this.setVisible(true); //todo from config manager
        processingSaveLocAndSize = false;
    }

    @Override
    protected void initialize() {
        super.initialize();
        currentNotes = new NotesLoader().getNotes("test");

        JPanel rootPanel = componentsFactory.getTransparentPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));

        contentPanel = new ContentPanel();
        rootPanel.add(contentPanel,BorderLayout.CENTER);
        rootPanel.add(getNavBar(),BorderLayout.PAGE_END);
        this.add(rootPanel,BorderLayout.CENTER);
        this.pack();
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
        JButton next = componentsFactory.getBorderedButton("Next");
        JButton close = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME,
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(AppThemeColor.BORDER),
                        BorderFactory.createLineBorder(AppThemeColor.TRANSPARENT, 3)
                ),
                "Close",
                14f);
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
            titleLabel = componentsFactory.getTextLabel("");
            initPanel();
        }

        private void initPanel() {
            this.add(titleLabel,BorderLayout.PAGE_START);
            renderCurrentNote();
        }
        private void renderCurrentNote(){
            Note note = currentNotes.get(noteIndex);
            titleLabel.setText(note.getTitle());

            NotesFrame.this.pack();
            NotesFrame.this.repaint();
        }
        public void next(){

        }
        public void prev(){

        }

    }
}
