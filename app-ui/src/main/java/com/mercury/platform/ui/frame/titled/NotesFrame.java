package com.mercury.platform.ui.frame.titled;

import com.mercury.platform.shared.ConfigManager;
import com.mercury.platform.shared.FrameVisibleState;
import com.mercury.platform.shared.store.MercuryStore;
import com.mercury.platform.ui.components.fields.font.FontStyle;
import com.mercury.platform.ui.components.fields.font.TextAlignment;
import com.mercury.platform.ui.frame.AbstractOverlaidFrame;
import com.mercury.platform.ui.frame.movable.ItemsGridFrame;
import com.mercury.platform.ui.manager.FramesManager;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.note.Note;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.List;

public class NotesFrame extends AbstractTitledComponentFrame {
    private List<Note> currentNotes;
    private ContentPanel contentPanel;
    private NotesType type;
    private ProgressBarFrame progressBarFrame;
    private boolean lastNote = false;

    private JCheckBox showOnStartUp;
    public NotesFrame(List<Note> notes, NotesType type) {
        super("MercuryTrade");
        this.currentNotes = notes;
        this.type = type;
        processEResize = false;
        processSEResize = false;
        progressBarFrame = new ProgressBarFrame();
        progressBarFrame.init();
        if(type.equals(NotesType.INFO)) {
            boolean showOnStartUp = ConfigManager.INSTANCE.isShowOnStartUp();
            if (showOnStartUp) {
                prevState = FrameVisibleState.SHOW;
            } else {
                this.setVisible(false);
                prevState = FrameVisibleState.HIDE;
            }
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
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
        if(type.equals(NotesType.INFO)) {
            miscPanel.add(showOnStartPanel, BorderLayout.CENTER);
        }
        miscPanel.add(getNavBar(),BorderLayout.PAGE_END);
        rootPanel.add(miscPanel,BorderLayout.PAGE_END);
        this.add(rootPanel,BorderLayout.CENTER);
        this.removeHideButton();
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
    private JPanel getNavBar(){
        JPanel navBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.RIGHT));
        Dimension dimension = new Dimension(80, 26);

        JButton download = componentsFactory.getBorderedButton("Download");
        download.addActionListener(action -> {
            download.setEnabled(false);
            progressBarFrame.setVisible(true);
            MercuryStore.INSTANCE.startUpdateSubject.onNext(true);
        });
        JButton gitHub = componentsFactory.getBorderedButton("GitHub");
        gitHub.addActionListener(action -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Exslims/MercuryTrade/releases"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        JButton donate = componentsFactory.getBorderedButton("Donate");
        donate.addActionListener(action -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=HJVSYP4YR7V88&lc=US&item_name=MercuryTrade&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        JButton close = componentsFactory.getButton(
                FontStyle.BOLD,
                AppThemeColor.FRAME,
                BorderFactory.createLineBorder(AppThemeColor.BORDER),
                "Close",
                14f);
        JButton next = componentsFactory.getBorderedButton("Next");
        next.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    contentPanel.next();
                    if (!contentPanel.hasNext()) {
                        lastNote = true;
                        navBar.add(close);
                    }
                    NotesFrame.this.pack();
                }
            }
        });
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
                if(SwingUtilities.isLeftMouseButton(e)) {
                    contentPanel.prev();
                    if (lastNote) {
                        navBar.remove(close);
                        lastNote = false;
                    }
                    NotesFrame.this.pack();
                    NotesFrame.this.repaint();
                }
            }
        });

        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    NotesFrame.this.setVisible(false);
                    if (type.equals(NotesType.INFO)) {
                        ConfigManager.INSTANCE.setShowOnStartUp(showOnStartUp.isSelected());
                        FramesManager.INSTANCE.enableMovementExclude(ItemsGridFrame.class);
                    }
                    prevState = FrameVisibleState.HIDE;
                }
            }
        });
        close.setBackground(AppThemeColor.FRAME);

        download.setPreferredSize(componentsFactory.convertSize(dimension));
        donate.setPreferredSize(componentsFactory.convertSize(dimension));
        gitHub.setPreferredSize(componentsFactory.convertSize(dimension));
        previous.setPreferredSize(componentsFactory.convertSize(dimension));
        next.setPreferredSize(componentsFactory.convertSize(dimension));
        close.setPreferredSize(componentsFactory.convertSize(dimension));

        if(type.equals(NotesType.PATCH) && !ConfigManager.INSTANCE.isShowPatchNotes()){
            navBar.add(donate);
            navBar.add(gitHub);
            navBar.add(download);
        }else {
            ConfigManager.INSTANCE.setShowPatchNotes(false);
        }
        if(currentNotes.size() == 1){
            navBar.add(close);
        }else {
            navBar.add(previous);
            navBar.add(next);
        }
        return navBar;
    }
    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Welcome to MercuryTrade";
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
            innerContent.setPreferredSize(new Dimension(530,270));

            JLabel image = componentsFactory.getIconLabel(note.getImagePath());
            JTextArea textArea = componentsFactory.getSimpleTextArea(note.getText());
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
        boolean hasNext(){
            return noteIndex < currentNotes.size()-1;
        }
        void prev(){
            if(noteIndex > 0){
                noteIndex--;
                renderCurrentNote();
            }
        }

    }
    public enum NotesType {
        INFO,
        PATCH
    }
    private class ProgressBarFrame extends AbstractOverlaidFrame {
        private JProgressBar progressBar;
        private JLabel percentLabel;
        private int percent;
        private JButton restart;
        private ProgressBarFrame() {
            super("MercuryTrade");
            percent = 0;
        }
        @Override
        protected void initialize() {
            this.setMinimumSize(new Dimension(310, 60));
            this.getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
            this.add(getProgressBarPanel(),BorderLayout.CENTER);
            this.pack();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        }

        private JPanel getProgressBarPanel() {
            JPanel root = componentsFactory.getTransparentPanel(new BorderLayout());

            JPanel barPanel = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.CENTER));
            progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setPreferredSize(new Dimension(300, 26));
            progressBar.setBackground(AppThemeColor.MSG_HEADER);
            progressBar.setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER));
            progressBar.setFont(componentsFactory.getFontByLang("0",FontStyle.ITALIC));
            barPanel.add(progressBar);

            root.add(barPanel,BorderLayout.CENTER);

            percentLabel = componentsFactory.getTextLabel(percent + "%");

            restart = componentsFactory.getBorderedButton("Restart");
            restart.addActionListener(action -> {
                restart.setEnabled(false);
                FramesManager.INSTANCE.exitForUpdate();
            });
            JPanel bottomBar = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomBar.add(percentLabel);
            bottomBar.add(restart);
            restart.setEnabled(false);
            restart.setPreferredSize(new Dimension(80, 26));
            root.add(bottomBar,BorderLayout.PAGE_END);
            return root;
        }

        @Override
        public void initHandlers() {
            MercuryStore.INSTANCE.chunkLoadedSubject.subscribe(percentDelta -> SwingUtilities.invokeLater(() -> {
                this.percent += percentDelta;
                this.percentLabel.setText(String.valueOf(percent) + "%");
                this.repaint();
                this.pack();
            }));
            MercuryStore.INSTANCE.updateReadySubject.subscribe(state -> {
                this.percentLabel.setText("100%");
                this.restart.setEnabled(true);
                this.progressBar.setIndeterminate(false);
                this.repaint();
            });
        }

        @Override
        protected LayoutManager getFrameLayout() {
            return new BorderLayout();
        }
    }
}
