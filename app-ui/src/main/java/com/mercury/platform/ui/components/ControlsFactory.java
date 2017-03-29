package com.mercury.platform.ui.components;


import com.mercury.platform.shared.events.EventRouter;
import com.mercury.platform.shared.events.custom.ChatCommandEvent;
import com.mercury.platform.ui.misc.AppThemeColor;
import com.mercury.platform.ui.misc.TooltipConstants;
import lombok.NonNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
