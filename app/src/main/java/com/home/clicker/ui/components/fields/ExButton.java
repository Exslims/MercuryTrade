package com.home.clicker.ui.components.fields;

import com.home.clicker.ui.components.interfaces.HasOpacity;
import com.home.clicker.ui.misc.AppThemeColor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Константин on 15.12.2016.
 */
public class ExButton extends JButton implements HasOpacity {
    public ExButton(String title) {
        super(title);
        init();
    }

    public ExButton(Icon icon) {
        super(icon);
        init();
    }

    private void init(){
        this.setBackground(AppThemeColor.BUTTON);
        this.setForeground(AppThemeColor.TEXT_DEFAULT);
        this.setFocusPainted(false);
        this.setFont(new Font("Tahoma",Font.BOLD,12));
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0,0,0,0),2),
                BorderFactory.createLineBorder(AppThemeColor.BUTTON,3)
        ));


        //todo
        this.addChangeListener(e -> {
            if(getModel().isPressed()){
//                setBackground(new Color(255,250,250,0));
            }else{
                setBackground(AppThemeColor.BUTTON);
            }
        });
    }

    @Override
    protected void paintBorder(Graphics g) {
        if(!getModel().isPressed()) {
            super.paintBorder(g);
        }
    }

    @Override
    public void setOpacity(int percent) {
        Color color = AppThemeColor.BUTTON;
        this.setBackground(new Color(color.getRed(),color.getGreen(),color.getBlue(),percent));
    }
}
