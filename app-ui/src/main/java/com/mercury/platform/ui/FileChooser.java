package com.mercury.platform.ui;
import com.mercury.platform.core.PrivateMessageManager;
import com.mercury.platform.core.misc.PatchNotifier;
import com.mercury.platform.shared.CachedFilesUtils;
import com.mercury.platform.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Константин on 09.12.2016.
 */
public class FileChooser extends OverlaidFrame {
    private String gamePath = "";
    private JFileChooser fileChooser;
    private JTextField textField;
    private JLabel errorLabel;
    public FileChooser() {
        super("Choose game path");
    }

    @Override
    protected void init() {
        super.init();
        setLayout(new BorderLayout());
        this.getRootPane().setBorder(BorderFactory.createLineBorder(AppThemeColor.BORDER,1));
        errorLabel = componentsFactory.getTextLabel("");
        errorLabel.setForeground(AppThemeColor.TEXT_IMPORTANT);

        JLabel title = componentsFactory.getTextLabel("Choose game path");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(title,BorderLayout.PAGE_START);

        fileChooser = new JFileChooser();
        fileChooser.setBackground(AppThemeColor.FRAME);
        textField = componentsFactory.getTextField("For example: C:/POE");

        JButton openButton = componentsFactory.getBorderedButton("Select");
        openButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(FileChooser.this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                gamePath = fileChooser.getSelectedFile().getPath();
                textField.setText(gamePath);
                FileChooser.this.repaint();
                pack();
            }
        });
        JButton saveButton = componentsFactory.getBorderedButton("Save");
        saveButton.addActionListener(e -> {
            if(CachedFilesUtils.isValidPath(gamePath)) {
                CachedFilesUtils.setGamePath(gamePath);
                FileChooser.this.setVisible(false);
                ExecutorService executor = Executors.newFixedThreadPool(4);
                executor.execute(() -> SwingUtilities.invokeLater(TaskBarFrame::new));
                executor.execute(PrivateMessageManager::new);
                executor.execute(PatchNotifier::new);
            }else {
                errorLabel.setText("Wrong game path!");
                pack();
            }
        });
        JPanel selectPanel = new JPanel();
        selectPanel.setBackground(AppThemeColor.TRANSPARENT);

        selectPanel.add(textField);
        selectPanel.add(openButton);
        selectPanel.add(saveButton);

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        disableHideEffect();
        this.add(selectPanel,BorderLayout.CENTER);
        this.add(errorLabel,BorderLayout.PAGE_END);
        pack();
    }

    @Override
    public void initHandlers() {

    }
}
