package com.mercury.platform.ui.frame;

import com.mercury.platform.shared.FrameStates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Константин on 19.02.2017.
 */
public class GamePathChooser extends TitledComponentFrame {

    public GamePathChooser() {
        super("MT-GamePathChooser");
//        prevState = FrameStates.HIDE;
        processEResize = false;
        processSEResize = false;
//        processingHideEvent = false;
//        this.setAlwaysOnTop(false);
//        this.setFocusableWindowState(true);
    }

    @Override
    protected void initialize() {
        super.initialize();

        JPanel root = componentsFactory.getTransparentPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField gamePath = componentsFactory.getTextField("Example: C:/Path of Exile/");
        gamePath.setPreferredSize(new Dimension(250,30));

        JButton select = componentsFactory.getBorderedButton("Select");
        select.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Choose game path");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                    gamePath.setText( chooser.getSelectedFile().getPath());
                }
            }
        });

        root.add(gamePath);
        root.add(select);
        this.add(root,BorderLayout.CENTER);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void initHandlers() {

    }

    @Override
    protected String getFrameTitle() {
        return "Select game path";
    }
}
