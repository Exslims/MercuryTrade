package com.home.clicker.ui;

import com.home.clicker.shared.HasEventHandlers;
import com.home.clicker.ui.components.ComponentsFactory;
import com.home.clicker.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 26.12.2016.
 */
public abstract class OverlaidFrame extends JFrame implements HasEventHandlers{
    protected ComponentsFactory componentsFactory = ComponentsFactory.INSTANCE;
    protected OverlaidFrame(String title){
        super(title);
        init();
    }
    protected void init(){
        getRootPane().setOpaque(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setOpacity(0.9f);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setFocusable(false);

        initHandlers();
    }
}
