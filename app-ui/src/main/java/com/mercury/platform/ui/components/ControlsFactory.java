package com.mercury.platform.ui.components;


import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.NonNull;

import javax.swing.*;

//todo generic for components constructor
public class ControlsFactory {
    private ComponentsFactory componentsFactory;
    public ControlsFactory(@NonNull ComponentsFactory factory){
        this.componentsFactory = factory;
    }
    public void getInviteButton(float iconSize) {
        JButton invite = componentsFactory.getIconButton(
                "app/invite.png",
                iconSize,
                AppThemeColor.SLIDE_BG,
                TooltipConstants.INVITE);
    }
}
