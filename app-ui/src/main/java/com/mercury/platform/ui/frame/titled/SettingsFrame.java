package com.mercury.platform.ui.frame.titled;


import com.mercury.platform.core.update.core.holder.ApplicationHolder;
import com.mercury.platform.shared.config.descriptor.FrameDescriptor;
import com.mercury.platform.shared.store.MercuryStoreCore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.panel.settings.*;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.MercuryStoreUI;
import com.mercury.platform.ui.misc.note.Note;
import com.mercury.platform.ui.misc.note.NotesLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class SettingsFrame extends AbstractTitledComponentFrame {
    private JPanel currentPanel;
    private MenuPanel menuPanel;
    private JPanel root;
    public SettingsFrame(){
        super();
        this.setFocusable(true);
        this.setFocusableWindowState(true);
        this.setAlwaysOnTop(false);
        this.processingHideEvent = false;
        this.processHideEffect = false;
//        FrameDescriptor frameDescriptor = this.framesConfig.get(this.getClass().getSimpleName());
        this.setPreferredSize(new Dimension(1000,600));
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.setPreferredSize(new Dimension(1000,600));
    }

    @Override
    public void onViewInit() {
        this.root = new JPanel(new BorderLayout());
        this.menuPanel = new MenuPanel();
        JPanel leftPanel = this.componentsFactory.getJPanel(new BorderLayout());
        leftPanel.add(this.menuPanel,BorderLayout.CENTER);
        leftPanel.add(this.getOperationsButtons(),BorderLayout.PAGE_END);
        this.add(leftPanel,BorderLayout.LINE_START);
        this.add(this.root,BorderLayout.CENTER);
        this.add(this.getBottomPanel(),BorderLayout.PAGE_END);
        this.pack();
    }

    @Override
    public void onSizeChange() {
        super.onSizeChange();
        FrameDescriptor frameDescriptor = this.framesConfig.get(this.getClass().getSimpleName());
        this.setPreferredSize(frameDescriptor.getFrameSize());
    }

    public void setContentPanel(JPanel panel){
        if(currentPanel != null){
            this.root.remove(currentPanel);
        }
        this.root.add(panel,BorderLayout.CENTER);
        this.currentPanel = panel;
        this.pack();
        this.repaint();
    }
    private JPanel getBottomPanel(){
        JPanel root = this.componentsFactory.getJPanel(new BorderLayout());
        root.setBorder(BorderFactory.createMatteBorder(1,0,0,0,AppThemeColor.MSG_HEADER_BORDER));
        root.setBackground(AppThemeColor.ADR_FOOTER_BG);
        root.add(this.getSaveButtonPanel(), BorderLayout.LINE_END);
        return root;
    }
    private JPanel getSaveButtonPanel(){
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(1,0));
        JButton saveButton = componentsFactory.getBorderedButton("Save",16);
        saveButton.addActionListener(e -> {
            MercuryStoreCore.showingDelaySubject.onNext(true);
            this.hideComponent();
            MercuryStoreUI.settingsSaveSubject.onNext(true);
        });
        JButton cancelButton = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME,
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                "Cancel",
                16f);
        cancelButton.addActionListener(e -> {
            MercuryStoreCore.showingDelaySubject.onNext(true);
            this.hideComponent();
            MercuryStoreUI.settingsRestoreSubject.onNext(true);
        });
        saveButton.setPreferredSize(new Dimension(110, 26));
        cancelButton.setPreferredSize(new Dimension(110, 26));
        root.add(this.componentsFactory.wrapToSlide(saveButton,AppThemeColor.HEADER,2,2,2,2));
        root.add(this.componentsFactory.wrapToSlide(cancelButton,AppThemeColor.HEADER,2,2,2,2));
        return root;
    }

    private JPanel getOperationsButtons(){
        JPanel root = componentsFactory.getTransparentPanel(new GridLayout(0,1,4,2));
        root.setBorder(BorderFactory.createMatteBorder(0,0,0,1,AppThemeColor.BORDER));
        JButton openTutorial = this.getOperationButton("Open tutorial","app/tutorial.png");
        openTutorial.addActionListener(action -> {
            FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
            FramesManager.INSTANCE.preShowFrame(NotesFrame.class);
        });
        JButton checkUpdates = this.getOperationButton("Check for updates", "app/check-update.png");
        checkUpdates.addActionListener(action -> {
            ApplicationHolder.getInstance().setManualRequest(true);
            MercuryStoreCore.requestPatchSubject.onNext(true);
        });
        JButton openTests = this.getOperationButton("Open tests","app/open-tests.png");
        openTests.addActionListener(action -> {
            FramesManager.INSTANCE.hideFrame(SettingsFrame.class);
            FramesManager.INSTANCE.preShowFrame(TestCasesFrame.class);
        });
        root.add(this.componentsFactory.wrapToSlide(openTutorial));
        root.add(this.componentsFactory.wrapToSlide(checkUpdates));
        root.add(this.componentsFactory.wrapToSlide(openTests));

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
//        root.add(patchNotes);
        return root;
    }

    private JButton getOperationButton(String title, String iconPath){
        JButton button = this.componentsFactory.getButton(title);
        button.setPreferredSize(new Dimension(210,35));
        button.setForeground(AppThemeColor.TEXT_DEFAULT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(AppThemeColor.ADR_BG);
        button.setFont(this.componentsFactory.getFont(FontStyle.BOLD,16f));
        button.setIcon(this.componentsFactory.getIcon(iconPath,22));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1,1,1,1,AppThemeColor.ADR_PANEL_BORDER),
                BorderFactory.createEmptyBorder(2,10,2,2)));
        return button;
    }

    @Override
    public void subscribe() {
        MercuryStoreUI.settingsRepaintSubject.subscribe(state -> {
            this.repaint();
        });
        MercuryStoreUI.settingsPackSubject.subscribe(state -> {
            this.pack();
        });
        MercuryStoreUI.adrManagerRepaint.subscribe(state -> {
            this.repaint();
        });
    }

    @Override
    protected String getFrameTitle() {
        return "Settings";
    }

    @Override
    public void hideComponent() {
        super.hideComponent();
        MercuryStoreCore.showingDelaySubject.onNext(true);
    }
}
