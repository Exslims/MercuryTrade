package com.mercury.platform.ui.components.panel.settings;

import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.components.ComponentsFactory;
import com.mercury.platform.ui.components.panel.misc.HasUI;
import com.mercury.platform.ui.frame.titled.NotesFrame;
import com.mercury.platform.ui.frame.titled.SettingsFrame;
import com.mercury.platform.ui.frame.titled.TestCasesFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.note.Note;
import com.mercury.platform.ui.misc.note.NotesLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HelpPanel extends JPanel implements HasUI {
    private ComponentsFactory componentsFactory;
    public HelpPanel() {
        super();
        componentsFactory = new ComponentsFactory();
        this.setBackground(AppThemeColor.TRANSPARENT);
        createUI();
    }

    @Override
    public void createUI() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton openTutorial = componentsFactory.getBorderedButton("Open tutorial");
        openTutorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
                    FramesManager.INSTANCE.preShowFrame(NotesFrame.class);
                }
            }
        });
        JButton openTests = componentsFactory.getBorderedButton("Open tests");
        openTests.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
                    FramesManager.INSTANCE.preShowFrame(TestCasesFrame.class);
                }
            }
        });
        JButton patchNotes = componentsFactory.getBorderedButton("Open patch notes");
        patchNotes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    NotesLoader notesLoader = new NotesLoader();
                    java.util.List<Note> patchNotes = notesLoader.getPatchNotes();
                    if(patchNotes.size() != 0){
                        NotesFrame patchNotesFrame = new NotesFrame(patchNotes, NotesFrame.NotesType.PATCH);
                        patchNotesFrame.init();
                        patchNotesFrame.showComponent();
                    }
                }
            }
        });
        JButton updateEvent = componentsFactory.getBorderedButton("Update event");
        updateEvent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    MercuryStore.INSTANCE.updateInfoSubject.onNext(123);
                    FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
                }
            }
        });
        this.add(openTutorial);
        this.add(openTests);
        this.add(patchNotes);
        this.add(updateEvent);
    }
}
